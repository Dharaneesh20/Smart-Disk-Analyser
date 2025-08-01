package com.diskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for partition information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartitionDTO {
    private Long id;
    private String name;
    private String label;
    private String fileSystem;
    private Long totalSpace;
    private String totalSpaceFormatted;
    private Long usedSpace;
    private String usedSpaceFormatted;
    private Long freeSpace;
    private String freeSpaceFormatted;
    private String mountPoint;
    private Boolean isSystemPartition;
    private Boolean isRemovable;
    private String healthStatus;
    private Double usagePercentage;
}
