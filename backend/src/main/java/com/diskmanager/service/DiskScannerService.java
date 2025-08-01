package com.diskmanager.service;

import com.diskmanager.dto.DiskStatisticsDTO;
import com.diskmanager.dto.FileInfoDTO;
import com.diskmanager.dto.ScanRequestDTO;
import com.diskmanager.model.FileInfo;
import com.diskmanager.repository.FileInfoRepository;
import com.diskmanager.util.FileSizeFormatter;
import com.diskmanager.util.FileTypeDetector;
import com.diskmanager.util.MD5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service for disk scanning and file analysis
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiskScannerService {
    
    private final FileInfoRepository fileInfoRepository;
    
    /**
     * Scan a directory and save file information
     */
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<List<FileInfoDTO>> scanDirectory(ScanRequestDTO request) {
        log.info("Starting scan of directory: {}", request.getPath());
        
        File directory = new File(request.getPath());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + request.getPath());
        }
        
        List<FileInfo> scannedFiles = new ArrayList<>();
        scanDirectoryRecursive(directory, scannedFiles, request, 0);
        
        // Save all files
        List<FileInfo> savedFiles = fileInfoRepository.saveAll(scannedFiles);
        
        log.info("Scan completed. Found {} files", savedFiles.size());
        
        List<FileInfoDTO> dtos = savedFiles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return CompletableFuture.completedFuture(dtos);
    }
    
    /**
     * Recursive directory scanning
     */
    private void scanDirectoryRecursive(File directory, List<FileInfo> fileList, 
                                       ScanRequestDTO request, int depth) {
        if (depth > request.getMaxDepth()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            // Skip hidden files if not included
            if (!request.getIncludeHidden() && file.isHidden()) {
                continue;
            }
            
            if (file.isDirectory()) {
                scanDirectoryRecursive(file, fileList, request, depth + 1);
            } else {
                try {
                    FileInfo fileInfo = createFileInfo(file);
                    
                    // Filter by extension if specified
                    if (request.getFileExtensions() != null && !request.getFileExtensions().isEmpty()) {
                        if (request.getFileExtensions().contains(fileInfo.getFileExtension())) {
                            fileList.add(fileInfo);
                        }
                    } else {
                        fileList.add(fileInfo);
                    }
                } catch (Exception e) {
                    log.warn("Error processing file {}: {}", file.getAbsolutePath(), e.getMessage());
                }
            }
        }
    }
    
    /**
     * Create FileInfo entity from File
     */
    private FileInfo createFileInfo(File file) throws IOException {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getName());
        fileInfo.setFilePath(file.getAbsolutePath());
        fileInfo.setFileSize(file.length());
        
        String extension = FileTypeDetector.getExtension(file.getName());
        fileInfo.setFileExtension(extension);
        fileInfo.setFileType(FileTypeDetector.detectFileType(extension));
        
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        fileInfo.setLastModified(LocalDateTime.ofInstant(
                attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
        
        fileInfo.setScannedAt(LocalDateTime.now());
        
        // Calculate MD5 hash for files smaller than 500MB
        if (file.length() < 500 * 1024 * 1024) {
            try {
                fileInfo.setMd5Hash(MD5Util.calculateMD5Smart(file.getAbsolutePath(), file.length()));
            } catch (IOException e) {
                log.warn("Failed to calculate MD5 for {}: {}", file.getName(), e.getMessage());
            }
        }
        
        return fileInfo;
    }
    
    /**
     * Find duplicate files based on MD5 hash
     */
    @Transactional
    public List<List<FileInfoDTO>> findDuplicates() {
        log.info("Searching for duplicate files");
        
        List<String> duplicateHashes = fileInfoRepository.findDuplicateHashes();
        Map<Long, List<FileInfoDTO>> duplicateGroups = new HashMap<>();
        
        long groupId = 1;
        for (String hash : duplicateHashes) {
            List<FileInfo> duplicates = fileInfoRepository.findAll().stream()
                    .filter(f -> hash.equals(f.getMd5Hash()))
                    .collect(Collectors.toList());
            
            if (duplicates.size() > 1) {
                final long currentGroupId = groupId;
                
                // Mark as duplicates and assign group ID
                duplicates.forEach(file -> {
                    file.setIsDuplicate(true);
                    file.setDuplicateGroupId(currentGroupId);
                });
                
                fileInfoRepository.saveAll(duplicates);
                
                List<FileInfoDTO> dtos = duplicates.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                
                duplicateGroups.put(groupId, dtos);
                groupId++;
            }
        }
        
        log.info("Found {} duplicate groups", duplicateGroups.size());
        return new ArrayList<>(duplicateGroups.values());
    }
    
    /**
     * Get large files above threshold
     */
    public List<FileInfoDTO> getLargeFiles(Long sizeThreshold) {
        log.info("Fetching files larger than {}", FileSizeFormatter.formatSize(sizeThreshold));
        
        List<FileInfo> largeFiles = fileInfoRepository.findLargeFiles(sizeThreshold);
        
        return largeFiles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get disk statistics
     */
    public DiskStatisticsDTO getDiskStatistics() {
        DiskStatisticsDTO stats = new DiskStatisticsDTO();
        
        // Total files and size
        long totalFiles = fileInfoRepository.count();
        Long totalSize = fileInfoRepository.findAll().stream()
                .mapToLong(FileInfo::getFileSize)
                .sum();
        
        stats.setTotalFiles(totalFiles);
        stats.setTotalSize(totalSize);
        stats.setTotalSizeFormatted(FileSizeFormatter.formatSize(totalSize));
        
        // Duplicate statistics
        Long duplicateCount = fileInfoRepository.countDuplicates();
        Long duplicateSize = fileInfoRepository.sumDuplicateSize();
        
        stats.setDuplicateFiles(duplicateCount != null ? duplicateCount : 0L);
        stats.setDuplicateSize(duplicateSize != null ? duplicateSize : 0L);
        stats.setDuplicateSizeFormatted(FileSizeFormatter.formatSize(
                duplicateSize != null ? duplicateSize : 0L));
        
        // File type distribution
        Map<String, Long> typeDistribution = new HashMap<>();
        Map<String, Long> sizeByType = new HashMap<>();
        
        List<Object[]> typeCounts = fileInfoRepository.countByFileType();
        for (Object[] row : typeCounts) {
            typeDistribution.put((String) row[0], (Long) row[1]);
        }
        
        List<Object[]> typeSizes = fileInfoRepository.sumSizeByFileType();
        for (Object[] row : typeSizes) {
            sizeByType.put((String) row[0], (Long) row[1]);
        }
        
        stats.setFileTypeDistribution(typeDistribution);
        stats.setSizeByFileType(sizeByType);
        
        // Large files (> 100MB)
        long largeFileThreshold = 100 * 1024 * 1024; // 100MB
        List<FileInfo> largeFiles = fileInfoRepository.findLargeFiles(largeFileThreshold);
        stats.setLargeFilesCount((long) largeFiles.size());
        stats.setLargeFilesSize(largeFiles.stream().mapToLong(FileInfo::getFileSize).sum());
        
        return stats;
    }
    
    /**
     * Convert FileInfo entity to DTO
     */
    private FileInfoDTO convertToDTO(FileInfo fileInfo) {
        FileInfoDTO dto = new FileInfoDTO();
        dto.setId(fileInfo.getId());
        dto.setFileName(fileInfo.getFileName());
        dto.setFilePath(fileInfo.getFilePath());
        dto.setFileSize(fileInfo.getFileSize());
        dto.setFileSizeFormatted(FileSizeFormatter.formatSize(fileInfo.getFileSize()));
        dto.setMd5Hash(fileInfo.getMd5Hash());
        dto.setFileExtension(fileInfo.getFileExtension());
        dto.setFileType(fileInfo.getFileType());
        dto.setLastModified(fileInfo.getLastModified() != null ? 
                fileInfo.getLastModified().toString() : null);
        dto.setIsDuplicate(fileInfo.getIsDuplicate());
        dto.setDuplicateGroupId(fileInfo.getDuplicateGroupId());
        return dto;
    }
}
