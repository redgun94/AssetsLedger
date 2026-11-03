package com.avocadotech.assetsledger.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequestDto(
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 120) String firstName,
        @NotBlank @Size(max = 150) String lastName,
        @NotBlank @Size(max = 15) String phone
) {
}
