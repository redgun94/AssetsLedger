package com.avocadotech.assetsledger.common.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank @Size(max = 150) String street,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 2) String state,
        @NotBlank @Size(max = 10) String zipCode

) {
}
