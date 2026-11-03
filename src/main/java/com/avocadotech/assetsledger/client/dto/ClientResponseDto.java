package com.avocadotech.assetsledger.client.dto;

import com.avocadotech.assetsledger.client.Client;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponseDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phone,
        Client.ServiceStatus serviceStatus,
        LocalDateTime createdAt
) {
}
