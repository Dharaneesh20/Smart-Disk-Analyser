package com.diskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for file information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileSizeFormatted;
    private String md5Hash;
    private String fileExtension;
    private String fileType;
    private String lastModified;
    private Boolean isDuplicate;
    private Long duplicateGroupId;
}
