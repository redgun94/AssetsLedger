package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.AddressDto;
import com.avocadotech.assetsledger.common.AddressMapper;
import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import com.avocadotech.assetsledger.company.exception.CompanyAlreadyExistsException;
import jakarta.validation.Valid;
import com.avocadotech.assetsledger.company.exception.CompanyFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponseDto create(@Valid CompanyRequestDto requestDto) {
        if (companyRepository.existsByCompanyNmls(requestDto.nmls())){
            throw new CompanyAlreadyExistsException(requestDto.nmls());
        }
        Company company = new Company();
        company.setCompanyNmls(requestDto.nmls());
        company.setName(requestDto.companyName());
        company.setAddress(AddressMapper.toEtity(requestDto.address()));
        return companyToResponseDto(company);
    }

    private CompanyResponseDto companyToResponseDto (Company company){
        AddressDto addressDto = AddressMapper.toAddressResponseDto(company.getAddress());
        return new CompanyResponseDto(
                company.getName(),
                company.getCompanyNmls(),
                company.getCompanyType(),
                addressDto
        );
    }

    public List<CompanyResponseDto> findAll() {
        return companyRepository.findAll().stream().map(this::companyToResponseDto).toList();
    }


    public CompanyResponseDto findByNmls(String nmls) {
        return companyToResponseDto(companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyFoundException("No Company found with this id " + nmls)));
    }
}
