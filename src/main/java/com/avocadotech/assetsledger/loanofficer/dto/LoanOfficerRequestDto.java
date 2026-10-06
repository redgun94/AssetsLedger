package com.avocadotech.assetsledger.loanofficer.dto;

import com.avocadotech.assetsledger.loanofficer.OfficerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoanOfficerRequestDto(
        @NotBlank String nmls,
        @NotNull OfficerType officerType,
        @NotNull UUID userId
        ) {}
