package com.diskmanager.controller;

import com.diskmanager.dto.PartitionDTO;
import com.diskmanager.service.PartitionService;
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
 * REST Controller for partition management operations
 */
@RestController
@RequestMapping("/api/partition")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Partition Manager", description = "APIs for partition management and analysis")
public class PartitionController {
    
    private final PartitionService partitionService;
    
    @GetMapping("/scan")
    @Operation(summary = "Scan partitions", description = "Scan and update all system partitions")
    public ResponseEntity<Map<String, Object>> scanPartitions() {
        log.info("Scanning system partitions");
        
        try {
            List<PartitionDTO> partitions = partitionService.scanPartitions();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partitions scanned successfully");
            response.put("count", partitions.size());
            response.put("partitions", partitions);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error scanning partitions", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error scanning partitions: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/list")
    @Operation(summary = "List partitions", description = "Get list of all partitions")
    public ResponseEntity<Map<String, Object>> listPartitions() {
        log.info("Listing all partitions");
        
        try {
            List<PartitionDTO> partitions = partitionService.getAllPartitions();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partitions retrieved successfully");
            response.put("count", partitions.size());
            response.put("partitions", partitions);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error listing partitions", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error listing partitions: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get partition details", description = "Get details of a specific partition")
    public ResponseEntity<Map<String, Object>> getPartition(@PathVariable Long id) {
        log.info("Fetching partition details for ID: {}", id);
        
        try {
            PartitionDTO partition = partitionService.getPartitionById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partition retrieved successfully");
            response.put("partition", partition);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching partition", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching partition: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Partition health check", description = "Get health status of all partitions")
    public ResponseEntity<Map<String, Object>> getPartitionHealth() {
        log.info("Checking partition health");
        
        try {
            List<PartitionDTO> partitions = partitionService.getPartitionHealth();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partition health checked successfully");
            response.put("partitions", partitions);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking partition health", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error checking partition health: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/{id}/resize")
    @Operation(summary = "Resize partition", description = "Resize a partition (simulated)")
    public ResponseEntity<Map<String, Object>> resizePartition(
            @PathVariable Long id, 
            @RequestParam Long newSize) {
        
        log.info("Resize request for partition {} to size {}", id, newSize);
        
        try {
            PartitionDTO partition = partitionService.resizePartition(id, newSize);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partition resize simulated (use system tools for actual resize)");
            response.put("partition", partition);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error resizing partition", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error resizing partition: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/{id}/extend")
    @Operation(summary = "Extend partition", description = "Extend a partition (simulated)")
    public ResponseEntity<Map<String, Object>> extendPartition(
            @PathVariable Long id, 
            @RequestParam Long additionalSpace) {
        
        log.info("Extend request for partition {} by {} bytes", id, additionalSpace);
        
        try {
            PartitionDTO partition = partitionService.extendPartition(id, additionalSpace);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Partition extend simulated (use system tools for actual extend)");
            response.put("partition", partition);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error extending partition", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error extending partition: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
