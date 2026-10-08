package com.avocadotech.assetsledger.company.dto;

import com.avocadotech.assetsledger.common.address.AddressDto;
import com.avocadotech.assetsledger.company.CompanyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompanyResponseDto(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 20) String companyNmls,
        @NotNull CompanyType companyType,
        @Valid AddressDto address
        ) {
}
