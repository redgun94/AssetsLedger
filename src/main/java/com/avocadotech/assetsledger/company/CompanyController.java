package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import com.avocadotech.assetsledger.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.avocadotech.assetsledger.company.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")


public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {

        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> create(@Valid @RequestBody CompanyRequestDto requestDto){
        CompanyResponseDto companyResponseDto = companyService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<CompanyResponseDto>>getAll(){
        List<CompanyResponseDto> companyList = companyService.findAll();
        return ResponseEntity.ok(companyList);
    }

    @GetMapping("/{nmls}")
    public ResponseEntity<CompanyResponseDto>getByNmls(@PathVariable String nmls){
        CompanyResponseDto companyResponseDto = companyService.findByNmls(nmls);
        return ResponseEntity.ok(companyResponseDto);
    }

    @DeleteMapping("/{nmls}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String nmls){
        companyService.deleteCompany(nmls);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{nmls}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable String nmls, @Valid @RequestBody CompanyRequestDto newRequestBody){
        CompanyResponseDto companyUpdatedResponseDto = companyService.updateByNmls(nmls,newRequestBody);
        return ResponseEntity.ok(companyUpdatedResponseDto);
    }
}
