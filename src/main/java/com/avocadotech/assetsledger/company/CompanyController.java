package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.avocadotech.assetsledger.company.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company")


public class CompanyController {
    CompanyService companyService;

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
    public ResponseEntity<CompanyResponseDto>getById(@PathVariable String nmls){
        CompanyResponseDto companyResponseDto = companyService.findByNmls(nmls);
        return ResponseEntity.ok(companyResponseDto);
    }
}
