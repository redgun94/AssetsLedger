package com.avocadotech.assetsledger.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @NotBlank @Size(max = 64) String name,
        @NotBlank @Size(max = 150) String lastName,
        @NotBlank @Size(max = 20) String phone
) {
}
