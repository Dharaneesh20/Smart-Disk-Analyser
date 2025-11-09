package com.diskmanager.controller;

import com.diskmanager.service.WindowsPartitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for advanced Windows partition operations
 * Requires administrator privileges
 */
@RestController
@RequestMapping("/api/partition/advanced")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Advanced Partition Operations", description = "Windows-specific partition management (requires admin)")
public class AdvancedPartitionController {
    
    private final WindowsPartitionService windowsPartitionService;
    
    /**
     * Check if running with administrator privileges
     */
    @GetMapping("/check-admin")
    @Operation(summary = "Check if running as administrator")
    public ResponseEntity<Map<String, Object>> checkAdmin() {
        Map<String, Object> response = new HashMap<>();
        boolean isAdmin = windowsPartitionService.isAdministrator();
        
        response.put("success", true);
        response.put("isAdministrator", isAdmin);
        response.put("message", isAdmin ? 
                "Running with administrator privileges" : 
                "Administrator privileges required for partition operations");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * List all physical disks
     */
    @GetMapping("/disks")
    @Operation(summary = "List all physical disks")
    public ResponseEntity<Map<String, Object>> listDisks() {
        try {
            List<WindowsPartitionService.DiskInfo> disks = windowsPartitionService.listDisks();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("disks", disks);
            response.put("count", disks.size());
            response.put("message", "Disks retrieved successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to list disks", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to list disks: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Create new partition
     */
    @PostMapping("/create")
    @Operation(summary = "Create new partition")
    public ResponseEntity<Map<String, Object>> createPartition(
            @RequestParam int diskNumber,
            @RequestParam String sizeInMB,
            @RequestParam(defaultValue = "NTFS") String fileSystem) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.createPartition(diskNumber, sizeInMB, fileSystem);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition created successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to create partition", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to create partition: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Delete partition
     */
    @DeleteMapping("/delete")
    @Operation(summary = "Delete partition")
    public ResponseEntity<Map<String, Object>> deletePartition(@RequestParam String volumeLetter) {
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.deletePartition(volumeLetter);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition deleted successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to delete partition", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to delete partition: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Format partition
     */
    @PostMapping("/format")
    @Operation(summary = "Format partition")
    public ResponseEntity<Map<String, Object>> formatPartition(
            @RequestParam String volumeLetter,
            @RequestParam String fileSystem,
            @RequestParam(required = false) String label) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.formatPartition(volumeLetter, fileSystem, label);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition formatted successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to format partition", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to format partition: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Extend partition
     */
    @PostMapping("/extend")
    @Operation(summary = "Extend partition size")
    public ResponseEntity<Map<String, Object>> extendPartition(
            @RequestParam String volumeLetter,
            @RequestParam String sizeInMB) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.extendPartition(volumeLetter, sizeInMB);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition extended successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to extend partition", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to extend partition: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Shrink partition
     */
    @PostMapping("/shrink")
    @Operation(summary = "Shrink partition size")
    public ResponseEntity<Map<String, Object>> shrinkPartition(
            @RequestParam String volumeLetter,
            @RequestParam String sizeInMB) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.shrinkPartition(volumeLetter, sizeInMB);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition shrunk successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to shrink partition", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to shrink partition: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Change drive letter
     */
    @PostMapping("/change-letter")
    @Operation(summary = "Change drive letter")
    public ResponseEntity<Map<String, Object>> changeDriveLetter(
            @RequestParam String currentLetter,
            @RequestParam String newLetter) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.changeDriveLetter(currentLetter, newLetter);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Drive letter changed successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to change drive letter", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to change drive letter: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Set partition as active (bootable)
     */
    @PostMapping("/set-active")
    @Operation(summary = "Set partition as active/bootable")
    public ResponseEntity<Map<String, Object>> setActivePartition(
            @RequestParam int diskNumber,
            @RequestParam int partitionNumber) {
        
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.setActivePartition(diskNumber, partitionNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Partition set as active successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to set partition as active", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to set partition as active: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Convert disk to GPT
     */
    @PostMapping("/convert-gpt")
    @Operation(summary = "Convert disk to GPT partition table")
    public ResponseEntity<Map<String, Object>> convertToGPT(@RequestParam int diskNumber) {
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.convertToGPT(diskNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Disk converted to GPT successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to convert to GPT", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to convert to GPT: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Convert disk to MBR
     */
    @PostMapping("/convert-mbr")
    @Operation(summary = "Convert disk to MBR partition table")
    public ResponseEntity<Map<String, Object>> convertToMBR(@RequestParam int diskNumber) {
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.convertToMBR(diskNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Disk converted to MBR successfully",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to convert to MBR", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to convert to MBR: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Clean disk (removes all partitions)
     */
    @PostMapping("/clean-disk")
    @Operation(summary = "Clean disk (WARNING: removes all partitions)")
    public ResponseEntity<Map<String, Object>> cleanDisk(@RequestParam int diskNumber) {
        try {
            if (!windowsPartitionService.isAdministrator()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Administrator privileges required"
                ));
            }
            
            String result = windowsPartitionService.cleanDisk(diskNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Disk cleaned successfully - ALL DATA HAS BEEN REMOVED",
                    "output", result
            ));
        } catch (Exception e) {
            log.error("Failed to clean disk", e);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Failed to clean disk: " + e.getMessage()
            ));
        }
    }
}
