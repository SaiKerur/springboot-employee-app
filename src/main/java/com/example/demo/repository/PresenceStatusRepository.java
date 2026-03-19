package com.example.demo.repository;

import com.example.demo.model.PresenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceStatusRepository extends JpaRepository<PresenceStatus, Integer> {
}
