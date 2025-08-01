package com.diskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a disk partition
 */
@Entity
@Table(name = "partitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // e.g., C:, D:, /dev/sda1
    
    @Column
    private String label;
    
    @Column(nullable = false)
    private String fileSystem; // NTFS, FAT32, ext4, etc.
    
    @Column(nullable = false)
    private Long totalSpace; // in bytes
    
    @Column(nullable = false)
    private Long usedSpace; // in bytes
    
    @Column(nullable = false)
    private Long freeSpace; // in bytes
    
    @Column
    private String mountPoint;
    
    @Column
    private Boolean isSystemPartition = false;
    
    @Column
    private Boolean isRemovable = false;
    
    @Column
    private String healthStatus; // HEALTHY, WARNING, CRITICAL
    
    @Column
    private Double usagePercentage;
}
