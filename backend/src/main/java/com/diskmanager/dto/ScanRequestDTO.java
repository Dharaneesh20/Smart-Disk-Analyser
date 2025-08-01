package com.diskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for scan request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanRequestDTO {
    private String path;
    private Boolean includeHidden = false;
    private Integer maxDepth = 10;
    private List<String> fileExtensions; // null means all extensions
}
