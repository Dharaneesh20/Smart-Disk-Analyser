package com.diskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a file in the system
 */
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false, length = 1000)
    private String filePath;
    
    @Column(nullable = false)
    private Long fileSize; // in bytes
    
    @Column(length = 32)
    private String md5Hash;
    
    @Column
    private String fileExtension;
    
    @Column
    private String fileType; // document, image, video, audio, etc.
    
    @Column
    private LocalDateTime lastModified;
    
    @Column
    private LocalDateTime scannedAt;
    
    @Column
    private Boolean isDuplicate = false;
    
    @Column
    private Long duplicateGroupId;
}
