package com.diskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for disk statistics response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiskStatisticsDTO {
    private Long totalFiles;
    private Long totalSize;
    private String totalSizeFormatted;
    private Long duplicateFiles;
    private Long duplicateSize;
    private String duplicateSizeFormatted;
    private Map<String, Long> fileTypeDistribution;
    private Map<String, Long> sizeByFileType;
    private Long largeFilesCount; // files > 100MB
    private Long largeFilesSize;
}
