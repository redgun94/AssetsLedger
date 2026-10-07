package com.avocadotech.assetsledger.loanofficer;

import com.avocadotech.assetsledger.company.Company;
import com.avocadotech.assetsledger.company.CompanyRepository;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerUpdateRequestDto;
import com.avocadotech.assetsledger.loanofficer.exception.LoanOfficerFoundException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsAlreadyExistsException;
import com.avocadotech.assetsledger.user.User;
import com.avocadotech.assetsledger.user.UserRepository;
import com.avocadotech.assetsledger.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanOfficerService {

    private final LoanOfficerRepository loanOfficerRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public LoanOfficerService(LoanOfficerRepository loanOfficerRepository, UserRepository userRepository, UserRepository userRepository1, CompanyRepository companyRepository) {
        this.loanOfficerRepository = loanOfficerRepository;
        this.userRepository = userRepository1;
        this.companyRepository = companyRepository;
    }

    public Optional<LoanOfficer> findByNmls(String nmls){
        return loanOfficerRepository.findByNmls(nmls);
    }
    public boolean existNmls(String nmls){
        return loanOfficerRepository.existsByNmls(nmls);
    }

    public LoanOfficerResponseDto create(@Valid LoanOfficerRequestDto request){
        if (loanOfficerRepository.existsByNmls(request.nmls())){
            throw new NmlsAlreadyExistsException(request.nmls());
        }
        Company company = companyRepository.findByCompanyNmls(request.companyNmls()).orElseThrow(()-> new CompanyNotFoundException(request.companyNmls()));
        User user = userRepository.findById(request.userId()).orElseThrow(()->new UserNotFoundException(request.userId()));
        LoanOfficer loanOfficer = new LoanOfficer();
        loanOfficer.setNmls(request.nmls());
        loanOfficer.setOfficerType(request.officerType());
        loanOfficer.setUser(user);
        loanOfficer.setCompany(company);

        LoanOfficer saved = loanOfficerRepository.save(loanOfficer);

        return new LoanOfficerResponseDto(saved.getNmls(),saved.getOfficerType());

    }
    public LoanOfficerResponseDto findById(UUID id){
        return loanOfficerRepository.findById(id).map(this::toResponseDto).orElseThrow(()->
                new LoanOfficerFoundException(id));
    }
    private LoanOfficerResponseDto toResponseDto(LoanOfficer loanOfficer){
        return new LoanOfficerResponseDto(loanOfficer.getNmls(),loanOfficer.getOfficerType());
    }

    public List<LoanOfficerResponseDto> findAll() {
        return loanOfficerRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public LoanOfficerResponseDto updateById(UUID id, LoanOfficerUpdateRequestDto request) {
        LoanOfficer loanOfficer = loanOfficerRepository.findById(id).orElseThrow(()-> new LoanOfficerFoundException(id));
        toNewUpdateLoanOfficer(loanOfficer,request);
        LoanOfficer saved = loanOfficerRepository.save(loanOfficer);
        return toResponseDto(saved);
    }

    private void toNewUpdateLoanOfficer(LoanOfficer loanOfficer, LoanOfficerUpdateRequestDto request) {
        loanOfficer.setNmls(request.nmls());
        loanOfficer.setOfficerType(request.officerType());
    }
}
