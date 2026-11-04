package com.avocadotech.assetsledger.company.exception;

import com.avocadotech.assetsledger.common.exception.ConflictException;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;

public class CompanyAlreadyExistsException extends ConflictException {

    private final CompanyResponseDto companyResponseDto;

    public CompanyAlreadyExistsException(CompanyResponseDto companyResponseDto) {
        super("A company with NMLS " + companyResponseDto.companyNmls() + " already exists.");

        this.companyResponseDto = companyResponseDto;
    }

    public CompanyResponseDto getCompanyResponseDto(){
        return companyResponseDto;
    }
}
