package com.avocadotech.assetsledger.company.dto;

import com.avocadotech.assetsledger.common.address.AddressDto;
import com.avocadotech.assetsledger.company.CompanyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyRequestDto(
       @NotBlank String nmls,
       @NotBlank String companyName,
       @NotNull CompanyType companyType,
       @Valid AddressDto address
) {
}
