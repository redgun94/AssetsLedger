package com.avocadotech.assetsledger.loanofficer;

import com.avocadotech.assetsledger.common.exception.InvalidSessionException;
import com.avocadotech.assetsledger.company.Company;
import com.avocadotech.assetsledger.company.CompanyRepository;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import com.avocadotech.assetsledger.loanofficer.exception.LoanOfficerNotFoundException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsAlreadyExistsException;
import com.avocadotech.assetsledger.loanofficer.exception.NmlsRequiredException;
import com.avocadotech.assetsledger.loanofficer.exception.ProfileAlreadyCompletedException;
import com.avocadotech.assetsledger.user.User;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de LoanOfficerService.createForUser(userId, request).
 *
 * Orden de validaciones del service:
 *   1. Un LOAN_OFFICER debe enviar NMLS (en blanco cuenta como ausente; no consulta la base).
 *   2. El usuario autenticado existe (si no, la sesión es inválida).
 *   3. El usuario todavía no tiene un perfil de loan officer.
 *   4. Si se envía NMLS personal, no está registrado por otro loan officer.
 *   5. La compañía existe.
 *   6. Se guarda el perfil y el usuario pasa a ACTIVE.
 */
@ExtendWith(MockitoExtension.class)
class LoanOfficerServiceTest {

    private static final String NMLS = "1234567";
    private static final String COMPANY_NMLS = "2045871";

    @Mock LoanOfficerRepository loanOfficerRepository;
    @Mock UserRepository userRepository;
    @Mock CompanyRepository companyRepository;

    @InjectMocks LoanOfficerService loanOfficerService;

    private UUID userId;
    private User user;
    private Company company;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setAccountStatus(User.Account_Status.PENDING_PROFILE);

        company = new Company();
        company.setId(UUID.randomUUID());
        company.setCompanyNmls(COMPANY_NMLS);
    }

    private void givenValidUserAndCompany() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(companyRepository.findByCompanyNmls(COMPANY_NMLS)).thenReturn(Optional.of(company));
        when(loanOfficerRepository.save(any(LoanOfficer.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private LoanOfficer capturedSave() {
        ArgumentCaptor<LoanOfficer> captor = ArgumentCaptor.forClass(LoanOfficer.class);
        verify(loanOfficerRepository).save(captor.capture());
        return captor.getValue();
    }

    // ---------- Camino feliz ----------

    @Test
    @DisplayName("createForUser: asocia usuario y compañía, guarda el perfil y activa la cuenta")
    void createForUser_linksUserAndCompany_andActivatesAccount() {
        var request = new LoanOfficerRequestDto(NMLS, OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        givenValidUserAndCompany();
        when(loanOfficerRepository.existsByNmls(NMLS)).thenReturn(false);

        LoanOfficerResponseDto response = loanOfficerService.createForUser(userId, request);

        LoanOfficer saved = capturedSave();
        assertThat(saved.getUser()).isSameAs(user);
        assertThat(saved.getCompany()).isSameAs(company);
        assertThat(saved.getNmls()).isEqualTo(NMLS);
        assertThat(saved.getOfficerType()).isEqualTo(OfficerType.LOAN_OFFICER);
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.ACTIVE);
        assertThat(response.nmls()).isEqualTo(NMLS);
        assertThat(response.officerType()).isEqualTo(OfficerType.LOAN_OFFICER);
    }

    @Test
    @DisplayName("createForUser: el NMLS se guarda sin espacios al inicio ni al final")
    void createForUser_trimsNmls() {
        var request = new LoanOfficerRequestDto("  " + NMLS + "  ", OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        givenValidUserAndCompany();
        when(loanOfficerRepository.existsByNmls(NMLS)).thenReturn(false);

        loanOfficerService.createForUser(userId, request);

        assertThat(capturedSave().getNmls()).isEqualTo(NMLS);
    }

    @Test
    @DisplayName("createForUser: un ASSISTANT puede registrarse sin NMLS y no se comprueba duplicado")
    void createForUser_allowsAssistantWithoutNmls() {
        var request = new LoanOfficerRequestDto(null, OfficerType.ASSISTANT, COMPANY_NMLS);
        givenValidUserAndCompany();

        loanOfficerService.createForUser(userId, request);

        LoanOfficer saved = capturedSave();
        assertThat(saved.getNmls()).isNull();
        assertThat(saved.getOfficerType()).isEqualTo(OfficerType.ASSISTANT);
        verify(loanOfficerRepository, never()).existsByNmls(anyString());
    }

    @Test
    @DisplayName("createForUser: un ASSISTANT con NMLS en blanco se guarda con NMLS null")
    void createForUser_blankNmlsForAssistantIsStoredAsNull() {
        var request = new LoanOfficerRequestDto("   ", OfficerType.ASSISTANT, COMPANY_NMLS);
        givenValidUserAndCompany();

        loanOfficerService.createForUser(userId, request);

        assertThat(capturedSave().getNmls()).isNull();
        verify(loanOfficerRepository, never()).existsByNmls(anyString());
    }

    // ---------- Reglas de negocio ----------

    @Test
    @DisplayName("createForUser: un LOAN_OFFICER sin NMLS es rechazado antes de consultar la base")
    void createForUser_throwsWhenLoanOfficerHasNoNmls() {
        var request = new LoanOfficerRequestDto(null, OfficerType.LOAN_OFFICER, COMPANY_NMLS);

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(NmlsRequiredException.class);

        verifyNoInteractions(userRepository, loanOfficerRepository, companyRepository);
    }

    @Test
    @DisplayName("createForUser: un LOAN_OFFICER con NMLS en blanco es rechazado")
    void createForUser_throwsWhenLoanOfficerHasBlankNmls() {
        var request = new LoanOfficerRequestDto("   ", OfficerType.LOAN_OFFICER, COMPANY_NMLS);

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(NmlsRequiredException.class);

        verifyNoInteractions(userRepository, loanOfficerRepository, companyRepository);
    }

    @Test
    @DisplayName("createForUser: un usuario no puede tener dos perfiles de loan officer")
    void createForUser_throwsWhenUserAlreadyHasProfile() {
        var request = new LoanOfficerRequestDto(NMLS, OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(ProfileAlreadyCompletedException.class);

        verify(loanOfficerRepository, never()).save(any());
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.PENDING_PROFILE);
    }

    @Test
    @DisplayName("createForUser: el NMLS personal no puede repetirse")
    void createForUser_throwsWhenNmlsAlreadyExists() {
        var request = new LoanOfficerRequestDto(NMLS, OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(loanOfficerRepository.existsByNmls(NMLS)).thenReturn(true);

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(NmlsAlreadyExistsException.class);

        verify(loanOfficerRepository, never()).save(any());
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.PENDING_PROFILE);
    }

    // ---------- Recursos inexistentes ----------

    @Test
    @DisplayName("createForUser: si el usuario del token no existe, la sesión es inválida")
    void createForUser_throwsWhenUserNotFound() {
        var request = new LoanOfficerRequestDto(NMLS, OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(InvalidSessionException.class);

        verify(loanOfficerRepository, never()).save(any());
    }

    @Test
    @DisplayName("createForUser: falla si la compañía no existe y la cuenta sigue pendiente")
    void createForUser_throwsWhenCompanyNotFound() {
        var request = new LoanOfficerRequestDto(NMLS, OfficerType.LOAN_OFFICER, COMPANY_NMLS);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(loanOfficerRepository.existsByUserId(userId)).thenReturn(false);
        when(loanOfficerRepository.existsByNmls(NMLS)).thenReturn(false);
        when(companyRepository.findByCompanyNmls(COMPANY_NMLS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanOfficerService.createForUser(userId, request))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(loanOfficerRepository, never()).save(any());
        assertThat(user.getAccountStatus()).isEqualTo(User.Account_Status.PENDING_PROFILE);
    }

    // ---------- Consulta del perfil ----------

    @Test
    @DisplayName("getByUserId: devuelve el perfil del usuario")
    void getByUserId_returnsProfile() {
        LoanOfficer loanOfficer = new LoanOfficer();
        loanOfficer.setNmls(NMLS);
        loanOfficer.setOfficerType(OfficerType.LOAN_OFFICER);
        when(loanOfficerRepository.findByUserId(userId)).thenReturn(Optional.of(loanOfficer));

        LoanOfficerResponseDto response = loanOfficerService.getByUserId(userId);

        assertThat(response.nmls()).isEqualTo(NMLS);
        assertThat(response.officerType()).isEqualTo(OfficerType.LOAN_OFFICER);
    }

    @Test
    @DisplayName("getByUserId: falla si el usuario aún no completó su perfil")
    void getByUserId_throwsWhenProfileNotFound() {
        when(loanOfficerRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanOfficerService.getByUserId(userId))
                .isInstanceOf(LoanOfficerNotFoundException.class);
    }
}
