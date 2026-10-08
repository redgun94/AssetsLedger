package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.address.Address;
import com.avocadotech.assetsledger.common.address.AddressDto;
import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import com.avocadotech.assetsledger.company.exception.CompanyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de CompanyService.
 *
 * Supuestos (ajusta lo que no coincida con tu código):
 *  - CompanyRequestDto(companyName, nmls, companyType, address)   <- revisa el ORDEN de tu record
 *  - Entidad Company: getName(), getCompanyNmls(), getCompanyType(), getAddress()
 *  - Repository: findByCompanyNmls(String), existsByCompanyNmls(String)
 *  - Service: create(request), getByNmls(nmls), updateByNmls(nmls, request), deleteCompany(nmls)
 *  - update y delete buscan con findByCompanyNmls(...).orElseThrow(...)
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    private static final String NMLS = "2045871";

    @Mock CompanyRepository companyRepository;

    @InjectMocks CompanyService companyService;

    private AddressDto addressDto;
    private Company existing;

    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("200 S Biscayne Blvd", "Miami", "FL", "33131");

        existing = new Company();
        existing.setName("Sunshine Home Lending LLC");
        existing.setCompanyNmls(NMLS);
        existing.setCompanyType(CompanyType.LENDER);
    }

    // ---------- create ----------

    @Test
    @DisplayName("create: guarda nombre, NMLS, tipo y dirección")
    void create_savesCompanyWithAddress() {
        var request = new CompanyRequestDto(NMLS,"Sunshine Home Lending LLC", CompanyType.LENDER, addressDto);
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        companyService.create(request);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        Company saved = captor.getValue();

        assertThat(saved.getName()).isEqualTo("Sunshine Home Lending LLC");
        assertThat(saved.getCompanyNmls()).isEqualTo(NMLS);
        assertThat(saved.getCompanyType()).isEqualTo(CompanyType.LENDER);

        Address address = saved.getAddress();
        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo("200 S Biscayne Blvd");
        assertThat(address.getCity()).isEqualTo("Miami");
        assertThat(address.getState()).isEqualTo("FL");
        assertThat(address.getZipCode()).isEqualTo("33131");
    }

    @Test
    @DisplayName("create: la dirección es opcional")
    void create_allowsCompanyWithoutAddress() {
        var request = new CompanyRequestDto(NMLS,"Sunshine Home Lending LLC", CompanyType.LENDER, null);
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        companyService.create(request);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getAddress()).isNull();
    }

    @Test
    @DisplayName("create: la respuesta lleva cada campo en su lugar")
    void create_returnsDtoWithFieldsInPlace() {
        var request = new CompanyRequestDto(NMLS,"Sunshine Home Lending LLC", CompanyType.LENDER, addressDto);
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyResponseDto response = companyService.create(request);


        assertThat(response.name()).isEqualTo("Sunshine Home Lending LLC");
        assertThat(response.companyNmls()).isEqualTo(NMLS);
        assertThat(response.companyType()).isEqualTo(CompanyType.LENDER);
        assertThat(response.address()).isEqualTo(addressDto);
    }

    // ---------- getByNmls ----------

    @Test
    @DisplayName("getByNmls: devuelve la compañía cuando existe")
    void getByNmls_returnsCompany() {
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.of(existing));

        CompanyResponseDto response = companyService.findByNmls(NMLS);

        assertThat(response.name()).isEqualTo("Sunshine Home Lending LLC");
        assertThat(response.companyNmls()).isEqualTo(NMLS);
    }

    @Test
    @DisplayName("getByNmls: lanza CompanyNotFoundException si no existe")
    void getByNmls_throwsWhenNotFound() {
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.findByNmls(NMLS))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    // ---------- updateByNmls ----------

    @Test
    @DisplayName("update: cambia nombre, tipo y dirección, y guarda")
    void update_changesFieldsAndSaves() {
        var request = new CompanyRequestDto(NMLS,"Sunshine Lending Group", CompanyType.BROKER, addressDto);
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.of(existing));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        companyService.updateByNmls(NMLS, request);

        verify(companyRepository).save(existing);
        assertThat(existing.getName()).isEqualTo("Sunshine Lending Group");
        assertThat(existing.getCompanyType()).isEqualTo(CompanyType.BROKER);
        assertThat(existing.getAddress()).isNotNull();
        assertThat(existing.getAddress().getCity()).isEqualTo("Miami");
    }

    @Test
    @DisplayName("update: lanza CompanyNotFoundException y no guarda nada")
    void update_throwsWhenNotFound() {
        var request = new CompanyRequestDto("Sunshine Lending Group", NMLS, CompanyType.BROKER, null);
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.updateByNmls(NMLS, request))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(companyRepository, never()).save(any());
    }

    // ---------- deleteCompany ----------

    @Test
    @DisplayName("delete: borra la compañía cuando existe")
    void delete_removesCompany() {
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.of(existing));

        companyService.deleteCompany(NMLS);

        verify(companyRepository).delete(existing);
    }

    @Test
    @DisplayName("delete: lanza CompanyNotFoundException y no borra nada")
    void delete_throwsWhenNotFound() {
        when(companyRepository.findByCompanyNmls(NMLS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.deleteCompany(NMLS))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(companyRepository, never()).delete(any());
    }
}