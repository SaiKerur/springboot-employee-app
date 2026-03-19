package com.example.demo.repository;

import com.example.demo.model.LiveNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveNotificationRepository extends JpaRepository<LiveNotification, Integer> {
}
