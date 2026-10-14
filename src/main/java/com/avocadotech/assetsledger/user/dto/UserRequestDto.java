package com.avocadotech.assetsledger.user.dto;

import com.avocadotech.assetsledger.user.User;
import org.springframework.stereotype.Component;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String name,
        @NotBlank String lastName,
        @NotBlank String phone
) {}