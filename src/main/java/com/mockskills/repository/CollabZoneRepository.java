package com.mockskills.repository;

import com.mockskills.model.CollabZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollabZoneRepository extends JpaRepository<CollabZone, Long> {
    boolean existsByEmail(String email);  // Custom method to check if email exists
}
