package com.avocadotech.assetsledger.loanofficer;

import com.avocadotech.assetsledger.company.Company;
import com.avocadotech.assetsledger.company.CompanyRepository;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.exception.LoanOfficerFoundException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsAlreadyExistsException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsRequiredException;
import com.avocadotech.assetsledger.loanofficer.exception.ProfileAlreadyCompletedException;
import com.avocadotech.assetsledger.user.User;
import com.avocadotech.assetsledger.user.exception.UserNotFoundException;
import com.avocadotech.assetsledger.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de LoanOfficerService.create(...)
 *
 * Orden de validaciones que asumen estas pruebas (de la más barata a la más costosa):
 *   1. Regla de negocio: un LOAN_OFFICER debe enviar NMLS (no consulta la base).
 *   2. El usuario existe.
 *   3. El usuario todavía no tiene un perfil de loan officer.
 *   4. El NMLS personal no está registrado por otro loan officer.
 *   5. La compañía existe.
 *   6. Se guarda el perfil y el usuario pasa a ACTIVE.
 *
 * Si tu service valida en otro orden, Mockito (modo estricto) avisará de stubs
 * que no se usaron. Ajusta el orden de los stubs o el del service.
 */
@ExtendWith(MockitoExtension.class)
class LoanOfficerServiceTest {

    @Mock LoanOfficerRepository loanOfficerRepository;
    @Mock UserRepository userRepository;
    @Mock CompanyRepository companyRepository;

    @InjectMocks LoanOfficerService loanOfficerService;

    private UUID userId;
    private UUID companyId;
    private User user;
    private Company company;
    final  String companyNmls = "2045871" ;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        companyId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setAccountStatus(User.Account_Status.PENDING_PROFILE);

        company = new Company();
        company.setId(companyId);
        company.setCompanyNmls("2045871");

    }

    // ---------- Camino feliz ----------

    @Test
    @DisplayName("create: asocia usuario y compañía, guarda el perfil y activa la cuenta")
    void create_linksUserAndCompany_andActivatesAccount() {
        // Arrange
        var request = new LoanOfficerRequestDto( "1234567", OfficerType.LOAN_OFFICER,userId, companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(loanOfficerRepository.existsByNmls("1234567")).thenReturn(false);
        when(companyRepository.findByCompanyNmls(companyNmls)).thenReturn(Optional.of(company));
        when(loanOfficerRepository.save(any(LoanOfficer.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanOfficerService.create(request);

        // Assert
        ArgumentCaptor<LoanOfficer> captor = ArgumentCaptor.forClass(LoanOfficer.class);
        verify(loanOfficerRepository).save(captor.capture());
        LoanOfficer saved = captor.getValue();

        assertThat(saved.getUser()).isSameAs(user);
        assertThat(saved.getCompany()).isSameAs(company);
        assertThat(saved.getNmls()).isEqualTo("1234567");
        assertThat(saved.getOfficerType()).isEqualTo(OfficerType.LOAN_OFFICER);
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.ACTIVE);
    }

    @Test
    @DisplayName("create: un ASSISTANT puede registrarse sin NMLS")
    void create_allowsAssistantWithoutNmls() {
        // Arrange
        var request = new LoanOfficerRequestDto(null, OfficerType.ASSISTANT, userId,companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(companyRepository.findByCompanyNmls(companyNmls)).thenReturn(Optional.of(company));
        when(loanOfficerRepository.save(any(LoanOfficer.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanOfficerService.create(request);

        // Assert
        ArgumentCaptor<LoanOfficer> captor = ArgumentCaptor.forClass(LoanOfficer.class);
        verify(loanOfficerRepository).save(captor.capture());
        assertThat(captor.getValue().getNmls()).isNull();
        assertThat(captor.getValue().getOfficerType()).isEqualTo(OfficerType.ASSISTANT);
    }

    // ---------- Reglas de negocio ----------

    @Test
    @DisplayName("create: un LOAN_OFFICER sin NMLS es rechazado y no se guarda nada")
    void create_throwsWhenLoanOfficerHasNoNmls() {
        var request = new LoanOfficerRequestDto(null, OfficerType.LOAN_OFFICER, userId,companyNmls);

        assertThatThrownBy(() -> loanOfficerService.create(request))
                .isInstanceOf(NmlsRequiredException.class);

        verify(loanOfficerRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: un usuario no puede tener dos perfiles de loan officer")
    void create_throwsWhenUserAlreadyHasProfile() {
        var request = new LoanOfficerRequestDto("1234567", OfficerType.LOAN_OFFICER, userId,companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> loanOfficerService.create(request))
                .isInstanceOf(LoanOfficerFoundException.class);

        verify(loanOfficerRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: el NMLS personal no puede repetirse")
    void create_throwsWhenNmlsAlreadyExists() {
        var request = new LoanOfficerRequestDto("1234567", OfficerType.LOAN_OFFICER, userId,companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(loanOfficerRepository.existsByNmls("1234567")).thenReturn(true);

        assertThatThrownBy(() -> loanOfficerService.create(request))
                .isInstanceOf(NmlsAlreadyExistsException.class);

        verify(loanOfficerRepository, never()).save(any());
    }

    // ---------- Recursos inexistentes ----------

    @Test
    @DisplayName("create: falla si el usuario no existe")
    void create_throwsWhenUserNotFound() {
        var request = new LoanOfficerRequestDto("1234567", OfficerType.LOAN_OFFICER, userId,companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanOfficerService.create(request))
                .isInstanceOf(UserNotFoundException.class);

        verify(loanOfficerRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: falla si la compañía no existe")
    void create_throwsWhenCompanyNotFound() {
        var request = new LoanOfficerRequestDto("1234567", OfficerType.LOAN_OFFICER, userId,companyNmls);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(loanOfficerRepository.existsByNmls("1234567")).thenReturn(false);
        when(companyRepository.findByCompanyNmls(companyNmls)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanOfficerService.create(request))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(loanOfficerRepository, never()).save(any());
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.PENDING_PROFILE);
    }
}