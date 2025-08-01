package com.diskmanager.repository;

import com.diskmanager.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FileInfo entity
 */
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    
    List<FileInfo> findByIsDuplicateTrue();
    
    List<FileInfo> findByDuplicateGroupId(Long groupId);
    
    @Query("SELECT f FROM FileInfo f WHERE f.fileSize > :sizeThreshold ORDER BY f.fileSize DESC")
    List<FileInfo> findLargeFiles(Long sizeThreshold);
    
    @Query("SELECT f.fileType, COUNT(f) FROM FileInfo f GROUP BY f.fileType")
    List<Object[]> countByFileType();
    
    @Query("SELECT f.fileType, SUM(f.fileSize) FROM FileInfo f GROUP BY f.fileType")
    List<Object[]> sumSizeByFileType();
    
    @Query("SELECT COUNT(f) FROM FileInfo f WHERE f.isDuplicate = true")
    Long countDuplicates();
    
    @Query("SELECT SUM(f.fileSize) FROM FileInfo f WHERE f.isDuplicate = true")
    Long sumDuplicateSize();
    
    @Query("SELECT f.md5Hash FROM FileInfo f GROUP BY f.md5Hash HAVING COUNT(f) > 1")
    List<String> findDuplicateHashes();
}
