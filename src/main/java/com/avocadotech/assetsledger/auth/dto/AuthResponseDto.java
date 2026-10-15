package com.avocadotech.assetsledger.auth.dto;

public record AuthResponseDto(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
