package com.avocadotech.assetsledger.loanofficer.dto;

import com.avocadotech.assetsledger.loanofficer.OfficerType;

public record LoanOfficerResponseDto(
        String nmls,
        OfficerType officerType
) {

}
