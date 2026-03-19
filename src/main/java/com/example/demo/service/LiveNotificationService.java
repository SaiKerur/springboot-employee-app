package com.example.demo.service;

import com.example.demo.dto.LiveNotificationRequestDTO;
import com.example.demo.dto.LiveNotificationResponseDTO;

import java.util.List;

public interface LiveNotificationService {

    LiveNotificationResponseDTO save(LiveNotificationRequestDTO notification);

    List<LiveNotificationResponseDTO> getAll();

    LiveNotificationResponseDTO getById(int id);

    LiveNotificationResponseDTO update(int id, LiveNotificationRequestDTO notification);

    void deleteById(int id);
}
