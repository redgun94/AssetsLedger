package com.avocadotech.assetsledger.loanofficer.dto;

import com.avocadotech.assetsledger.loanofficer.OfficerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoanOfficerUpdateRequestDto(
        @Size(max = 20) String nmls,
        @Size(max = 20) OfficerType officerType
) {
}
