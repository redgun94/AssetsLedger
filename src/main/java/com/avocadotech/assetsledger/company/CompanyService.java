package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.address.AddressDto;
import com.avocadotech.assetsledger.common.address.AddressMapper;
import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import com.avocadotech.assetsledger.company.exception.CompanyAlreadyExistsException;
import com.avocadotech.assetsledger.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
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
        return companyToResponseDto(companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException("No Company found with this id " + nmls)));
    }

    @Transactional
    public void deleteCompany(String nmls){

        Company company = companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException(nmls));
        companyRepository.delete(company);
    }

    public CompanyResponseDto updateByNmls(String nmls, CompanyRequestDto newCompanyData) {
        Company company = companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException(nmls));
        company.setName(newCompanyData.companyName());
        company.setCompanyNmls(newCompanyData.nmls());
        company.setCompanyType(newCompanyData.companyType());
        company.setAddress(AddressMapper.toEntity(newCompanyData.address()));
        return companyToResponseDto(companyRepository.save(company));
    }
}
