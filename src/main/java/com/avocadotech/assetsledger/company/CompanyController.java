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

    @GetMapping("/{nmls}")
    public ResponseEntity<CompanyResponseDto>getByNmls(@PathVariable String nmls){
        CompanyResponseDto companyResponseDto = companyService.findByNmls(nmls);
        return ResponseEntity.ok(companyResponseDto);
    }
}
