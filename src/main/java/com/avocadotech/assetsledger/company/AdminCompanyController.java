package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/companies")
public class AdminCompanyController {

    private final CompanyService companyService;

    public AdminCompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDto>> getAll(){
        return ResponseEntity.ok(companyService.findAll());
    }

    @PutMapping("/{nmls}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable String nmls, @Valid @RequestBody CompanyRequestDto newRequestBody){
        return ResponseEntity.ok(companyService.updateByNmls(nmls, newRequestBody));
    }

    @DeleteMapping("/{nmls}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String nmls){
        companyService.deleteCompany(nmls);
        return ResponseEntity.noContent().build();
    }
}
