package com.avocadotech.assetsledger.loanofficer.dto;

import com.avocadotech.assetsledger.loanofficer.OfficerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record LoanOfficerRequestDto(
        @Size(max = 20) String nmls,
        @NotNull OfficerType officerType,
        @NotBlank String companyNmls
        ) {}
