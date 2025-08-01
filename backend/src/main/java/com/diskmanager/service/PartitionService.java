package com.diskmanager.service;

import com.diskmanager.dto.PartitionDTO;
import com.diskmanager.model.Partition;
import com.diskmanager.repository.PartitionRepository;
import com.diskmanager.util.FileSizeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for partition management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PartitionService {
    
    private final PartitionRepository partitionRepository;
    
    /**
     * Scan and update all system partitions
     */
    @Transactional
    public List<PartitionDTO> scanPartitions() {
        log.info("Scanning system partitions");
        
        // Clear existing data
        partitionRepository.deleteAll();
        
        List<Partition> partitions = new ArrayList<>();
        File[] roots = File.listRoots();
        
        for (File root : roots) {
            try {
                Partition partition = createPartitionInfo(root);
                partitions.add(partition);
            } catch (Exception e) {
                log.warn("Error scanning partition {}: {}", root.getAbsolutePath(), e.getMessage());
            }
        }
        
        List<Partition> savedPartitions = partitionRepository.saveAll(partitions);
        
        log.info("Found {} partitions", savedPartitions.size());
        
        return savedPartitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create Partition entity from File root
     */
    private Partition createPartitionInfo(File root) {
        Partition partition = new Partition();
        
        partition.setName(root.getAbsolutePath());
        partition.setMountPoint(root.getAbsolutePath());
        
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        
        partition.setTotalSpace(totalSpace);
        partition.setFreeSpace(freeSpace);
        partition.setUsedSpace(usedSpace);
        
        // Calculate usage percentage
        double usagePercentage = totalSpace > 0 ? 
                ((double) usedSpace / totalSpace) * 100 : 0;
        partition.setUsagePercentage(usagePercentage);
        
        // Determine file system (simplified for cross-platform compatibility)
        partition.setFileSystem(detectFileSystem(root));
        
        // Check if system partition (usually C: on Windows or / on Unix)
        partition.setIsSystemPartition(isSystemPartition(root.getAbsolutePath()));
        
        // Check if removable (simplified detection)
        partition.setIsRemovable(false); // Would need JNA for proper detection
        
        // Determine health status based on usage
        partition.setHealthStatus(determineHealthStatus(usagePercentage));
        
        return partition;
    }
    
    /**
     * Detect file system type
     */
    private String detectFileSystem(File root) {
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            return "NTFS"; // Most Windows systems use NTFS
        } else if (os.contains("mac")) {
            return "APFS"; // Modern macOS uses APFS
        } else {
            return "ext4"; // Most Linux systems use ext4
        }
    }
    
    /**
     * Check if partition is system partition
     */
    private boolean isSystemPartition(String path) {
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            return path.toUpperCase().startsWith("C:");
        } else {
            return path.equals("/");
        }
    }
    
    /**
     * Determine health status based on usage percentage
     */
    private String determineHealthStatus(double usagePercentage) {
        if (usagePercentage >= 90) {
            return "CRITICAL";
        } else if (usagePercentage >= 80) {
            return "WARNING";
        } else {
            return "HEALTHY";
        }
    }
    
    /**
     * Get all partitions
     */
    public List<PartitionDTO> getAllPartitions() {
        return partitionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get partition by ID
     */
    public PartitionDTO getPartitionById(Long id) {
        Partition partition = partitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partition not found: " + id));
        
        return convertToDTO(partition);
    }
    
    /**
     * Get partition health summary
     */
    public List<PartitionDTO> getPartitionHealth() {
        List<Partition> partitions = partitionRepository.findAll();
        
        // Refresh partition data
        for (Partition partition : partitions) {
            File root = new File(partition.getMountPoint());
            if (root.exists()) {
                long totalSpace = root.getTotalSpace();
                long freeSpace = root.getFreeSpace();
                long usedSpace = totalSpace - freeSpace;
                
                partition.setFreeSpace(freeSpace);
                partition.setUsedSpace(usedSpace);
                
                double usagePercentage = totalSpace > 0 ? 
                        ((double) usedSpace / totalSpace) * 100 : 0;
                partition.setUsagePercentage(usagePercentage);
                partition.setHealthStatus(determineHealthStatus(usagePercentage));
            }
        }
        
        partitionRepository.saveAll(partitions);
        
        return partitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Simulate partition resize (educational purposes)
     * In production, this would use native tools like diskpart or parted
     */
    public PartitionDTO resizePartition(Long id, Long newSize) {
        log.info("Resize partition request for ID {} to size {}", id, newSize);
        
        Partition partition = partitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partition not found: " + id));
        
        // Validate new size
        if (newSize < partition.getUsedSpace()) {
            throw new IllegalArgumentException(
                    "New size cannot be smaller than used space: " + 
                    FileSizeFormatter.formatSize(partition.getUsedSpace()));
        }
        
        // In a real implementation, this would call native partition tools
        log.warn("Partition resize is a simulated operation. Use system tools for actual resizing.");
        
        return convertToDTO(partition);
    }
    
    /**
     * Simulate partition extend (educational purposes)
     */
    public PartitionDTO extendPartition(Long id, Long additionalSpace) {
        log.info("Extend partition request for ID {} by {}", id, additionalSpace);
        
        Partition partition = partitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partition not found: " + id));
        
        // In a real implementation, this would call native partition tools
        log.warn("Partition extend is a simulated operation. Use system tools for actual extending.");
        
        return convertToDTO(partition);
    }
    
    /**
     * Convert Partition entity to DTO
     */
    private PartitionDTO convertToDTO(Partition partition) {
        PartitionDTO dto = new PartitionDTO();
        dto.setId(partition.getId());
        dto.setName(partition.getName());
        dto.setLabel(partition.getLabel());
        dto.setFileSystem(partition.getFileSystem());
        dto.setTotalSpace(partition.getTotalSpace());
        dto.setTotalSpaceFormatted(FileSizeFormatter.formatSize(partition.getTotalSpace()));
        dto.setUsedSpace(partition.getUsedSpace());
        dto.setUsedSpaceFormatted(FileSizeFormatter.formatSize(partition.getUsedSpace()));
        dto.setFreeSpace(partition.getFreeSpace());
        dto.setFreeSpaceFormatted(FileSizeFormatter.formatSize(partition.getFreeSpace()));
        dto.setMountPoint(partition.getMountPoint());
        dto.setIsSystemPartition(partition.getIsSystemPartition());
        dto.setIsRemovable(partition.getIsRemovable());
        dto.setHealthStatus(partition.getHealthStatus());
        dto.setUsagePercentage(partition.getUsagePercentage());
        return dto;
    }
}
