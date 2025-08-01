package com.diskmanager.repository;

import com.diskmanager.model.Partition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Partition entity
 */
@Repository
public interface PartitionRepository extends JpaRepository<Partition, Long> {
    
    Optional<Partition> findByName(String name);
    
    List<Partition> findByIsSystemPartitionTrue();
    
    List<Partition> findByIsRemovableTrue();
    
    List<Partition> findByHealthStatus(String healthStatus);
}
