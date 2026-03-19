package com.example.demo.service;

import com.example.demo.dto.PresenceStatusRequestDTO;
import com.example.demo.dto.PresenceStatusResponseDTO;

import java.util.List;

public interface PresenceStatusService {

    PresenceStatusResponseDTO save(PresenceStatusRequestDTO presence);

    List<PresenceStatusResponseDTO> getAll();

    PresenceStatusResponseDTO getById(int id);

    PresenceStatusResponseDTO update(int id, PresenceStatusRequestDTO presence);

    void deleteById(int id);
}
