package com.avocadotech.assetsledger.loanofficer;

import com.avocadotech.assetsledger.common.exception.InvalidSessionException;
import com.avocadotech.assetsledger.company.Company;
import com.avocadotech.assetsledger.company.CompanyRepository;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerUpdateRequestDto;
import com.avocadotech.assetsledger.loanofficer.exception.LoanOfficerNotFoundException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsAlreadyExistsException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsRequiredException;
import com.avocadotech.assetsledger.loanofficer.exception.ProfileAlreadyCompletedException;
import com.avocadotech.assetsledger.user.User;
import com.avocadotech.assetsledger.user.UserRepository;
import com.avocadotech.assetsledger.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /*public LoanOfficerResponseDto create(@Valid LoanOfficerRequestDto request){

        if(request.officerType() == OfficerType.LOAN_OFFICER && request.nmls() == null){
            throw new NmlsRequiredException("Loan Officer without a valid NMLS is not allowed ");
        }
        User user = userRepository.findById(request.userId()).orElseThrow(()->new UserNotFoundException(request.userId()));

        if (loanOfficerRepository.existsByUserId(request.userId())){
            throw new LoanOfficerNotFoundException(user.getId());
        }
        if (loanOfficerRepository.existsByNmls(request.nmls())){
            throw new NmlsAlreadyExistsException(request.nmls());
        }

        Company company = companyRepository.findByCompanyNmls(request.companyNmls()).orElseThrow(()-> new CompanyNotFoundException(request.companyNmls()));
        LoanOfficer loanOfficer = new LoanOfficer();
        loanOfficer.setNmls(request.nmls());
        loanOfficer.setOfficerType(request.officerType());
        loanOfficer.setUser(user);
        loanOfficer.setCompany(company);
        user.setAccountStatus(User.Account_Status.ACTIVE);
        LoanOfficer saved = loanOfficerRepository.save(loanOfficer);

        return new LoanOfficerResponseDto(saved.getNmls(),saved.getOfficerType());

    }*/
    public LoanOfficerResponseDto findById(UUID id){
        return loanOfficerRepository.findById(id).map(this::toResponseDto).orElseThrow(()->
                new LoanOfficerNotFoundException(id));
    }
    private LoanOfficerResponseDto toResponseDto(LoanOfficer loanOfficer){
        return new LoanOfficerResponseDto(loanOfficer.getNmls(),loanOfficer.getOfficerType());
    }

    public List<LoanOfficerResponseDto> findAll() {
        return loanOfficerRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public LoanOfficerResponseDto updateByUserId(UUID userId, LoanOfficerUpdateRequestDto request) {
        LoanOfficer loanOfficer = findProfileOf(userId);
        toNewUpdateLoanOfficer(loanOfficer, request);
        LoanOfficer saved = loanOfficerRepository.save(loanOfficer);
        return toResponseDto(saved);
    }

    private void toNewUpdateLoanOfficer(LoanOfficer loanOfficer, LoanOfficerUpdateRequestDto request) {
        loanOfficer.setNmls(request.nmls());
        loanOfficer.setOfficerType(request.officerType());
    }

    @Transactional
    public LoanOfficerResponseDto createForUser(UUID userId, LoanOfficerRequestDto request) {
        String nmls = normalizeNmls(request.nmls());
        if (request.officerType() == OfficerType.LOAN_OFFICER && nmls == null) {
            throw new NmlsRequiredException("NMLS is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidSessionException());

        if (loanOfficerRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyCompletedException(userId);
        }
        if (nmls != null && loanOfficerRepository.existsByNmls(nmls)) {
            throw new NmlsAlreadyExistsException(nmls);
        }
        Company company = companyRepository.findByCompanyNmls(request.companyNmls())
                .orElseThrow(() -> new CompanyNotFoundException(request.companyNmls()));

        LoanOfficer loanOfficer = new LoanOfficer();
        loanOfficer.setUser(user);
        loanOfficer.setCompany(company);
        loanOfficer.setNmls(nmls);
        loanOfficer.setOfficerType(request.officerType());
        user.setAccountStatus(User.Account_Status.ACTIVE);

        return toResponseDto(loanOfficerRepository.save(loanOfficer));
    }

    public LoanOfficerResponseDto getByUserId(UUID userId) {
        return toResponseDto(findProfileOf(userId));
    }

    public LoanOfficer findProfileOf(UUID userId) {
        return loanOfficerRepository.findByUserId(userId)
                .orElseThrow(() -> new LoanOfficerNotFoundException(userId));   // 404: aún no completó el onboarding
    }
    private String normalizeNmls(String nmls) {
        if (nmls == null || nmls.isBlank()) {
            return null;
        }
        return nmls.trim();
    }
}
