package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.AddressDto;
import com.avocadotech.assetsledger.common.AddressMapper;
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
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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
        return companyToResponseDto(companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException("No Company found with this id " + nmls)));
    }

    @Transactional
    public void deleteCompany(String nmls){
        if(!companyRepository.existsByCompanyNmls(nmls)){
            throw new CompanyNotFoundException(nmls);
        }
        Company company = companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException(nmls));
        companyRepository.delete(company);
    }

    public CompanyResponseDto updateByNmls(String nmls, CompanyRequestDto newCompanyData) {
        Company company = companyRepository.findByCompanyNmls(nmls).orElseThrow(()-> new CompanyNotFoundException(nmls));
        company.setName(newCompanyData.companyName());
        company.setCompanyNmls(newCompanyData.nmls());
        company.setCompanyType(newCompanyData.companyType());
        company.setAddress(AddressMapper.toEtity(newCompanyData.address()));
        return companyToResponseDto(companyRepository.save(company));
    }
}
