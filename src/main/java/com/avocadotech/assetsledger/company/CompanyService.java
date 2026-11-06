package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.address.AddressDto;
import com.avocadotech.assetsledger.common.address.AddressMapper;
import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import com.avocadotech.assetsledger.company.exception.CompanyAlreadyExistsException;
import jakarta.transaction.Transactional;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponseDto create(CompanyRequestDto requestDto) {
        String nmls = requestDto.nmls().trim();
        companyRepository.findByCompanyNmls(nmls).ifPresent(existing ->{
                    throw new CompanyAlreadyExistsException(companyToResponseDto(existing));});
        Company newCompany = new Company();
        newCompany.setCompanyNmls(nmls);
        newCompany.setName(requestDto.companyName());
        newCompany.setCompanyType(requestDto.companyType());
        newCompany.setAddress(AddressMapper.toEntity(requestDto.address()));
        Company companySaved = companyRepository.save(newCompany);
        return companyToResponseDto(companySaved);
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
        String normalizedNmls = nmls.trim();
        return companyToResponseDto(companyRepository.findByCompanyNmls(normalizedNmls).orElseThrow(()-> new CompanyNotFoundException("No Company found with this NMLS " + normalizedNmls)));
    }

    @Transactional
    public void deleteCompany(String nmls){
        String normalizedNmls = nmls.trim();
        Company company = companyRepository.findByCompanyNmls(normalizedNmls).orElseThrow(()-> new CompanyNotFoundException(normalizedNmls));
        companyRepository.delete(company);
    }

    public CompanyResponseDto updateByNmls(String nmls, CompanyRequestDto newCompanyData) {
        String normalizedNmls = nmls.trim();
        Company company = companyRepository.findByCompanyNmls(normalizedNmls).orElseThrow(()-> new CompanyNotFoundException(normalizedNmls));
        company.setName(newCompanyData.companyName());
        company.setCompanyNmls(newCompanyData.nmls().trim());
        company.setCompanyType(newCompanyData.companyType());
        company.setAddress(AddressMapper.toEntity(newCompanyData.address()));
        return companyToResponseDto(companyRepository.save(company));
    }
}
