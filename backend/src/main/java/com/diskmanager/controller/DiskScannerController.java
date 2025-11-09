package com.diskmanager.controller;

import com.diskmanager.dto.DiskStatisticsDTO;
import com.diskmanager.dto.FileInfoDTO;
import com.diskmanager.dto.ScanRequestDTO;
import com.diskmanager.service.DiskScannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for disk scanning operations
 */
@RestController
@RequestMapping("/api/disk")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Disk Scanner", description = "APIs for disk scanning and file analysis")
public class DiskScannerController {
    
    private final DiskScannerService diskScannerService;
    
    @PostMapping("/scan")
    @Operation(summary = "Scan a directory", description = "Recursively scan a directory and analyze files")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> scanDirectory(
            @RequestBody ScanRequestDTO request) {
        
        log.info("Received scan request for path: {}", request.getPath());
        
        return diskScannerService.scanDirectory(request)
                .thenApply(files -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Scan completed successfully");
                    response.put("filesScanned", files.size());
                    response.put("files", files);
                    
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    log.error("Error scanning directory", ex);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Error scanning directory: " + ex.getMessage());
                    
                    return ResponseEntity.badRequest().body(errorResponse);
                });
    }
    
    @GetMapping("/duplicates")
    @Operation(summary = "Find duplicate files", description = "Find duplicate files based on MD5 hash")
    public ResponseEntity<Map<String, Object>> findDuplicates() {
        log.info("Finding duplicate files");
        
        try {
            List<List<FileInfoDTO>> duplicates = diskScannerService.findDuplicates();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Duplicate search completed");
            response.put("duplicateGroups", duplicates.size());
            response.put("duplicates", duplicates);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error finding duplicates", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error finding duplicates: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/large-files")
    @Operation(summary = "Get large files", description = "Get files larger than specified size")
    public ResponseEntity<Map<String, Object>> getLargeFiles(
            @RequestParam(defaultValue = "104857600") Long sizeThreshold) { // Default 100MB
        
        log.info("Fetching large files with threshold: {} bytes", sizeThreshold);
        
        try {
            List<FileInfoDTO> largeFiles = diskScannerService.getLargeFiles(sizeThreshold);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Large files retrieved successfully");
            response.put("count", largeFiles.size());
            response.put("files", largeFiles);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving large files", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving large files: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "Get disk statistics", description = "Get comprehensive disk usage statistics")
    public ResponseEntity<Map<String, Object>> getDiskStatistics() {
        log.info("Fetching disk statistics");
        
        try {
            DiskStatisticsDTO stats = diskScannerService.getDiskStatistics();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistics retrieved successfully");
            response.put("statistics", stats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving statistics", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving statistics: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the disk scanner API is running")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Disk Scanner API is running");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
