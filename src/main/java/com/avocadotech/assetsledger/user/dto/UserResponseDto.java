package com.avocadotech.assetsledger.user.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String lastName,
        String email
) {}
