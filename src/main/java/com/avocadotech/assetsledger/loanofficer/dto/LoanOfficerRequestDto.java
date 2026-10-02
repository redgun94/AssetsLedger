package com.avocadotech.assetsledger.loanofficer.dto;

import com.avocadotech.assetsledger.loanofficer.OfficerType;
import jakarta.validation.constraints.NotBlank;

public record LoanOfficerRequestDto(
        @NotBlank String nmls,
        @NotBlank OfficerType officerType
        ) {}
