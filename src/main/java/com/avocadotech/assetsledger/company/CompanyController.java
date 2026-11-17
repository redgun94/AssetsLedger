package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.company.dto.CompanyRequestDto;
import com.avocadotech.assetsledger.company.dto.CompanyResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CompanyResponseDto> getByNmls(@PathVariable String nmls){
        CompanyResponseDto companyResponseDto = companyService.findByNmls(nmls);
        return ResponseEntity.ok(companyResponseDto);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<CompanyResponseDto>> findByName(@RequestParam @NotBlank String name){
        return ResponseEntity.ok(companyService.findByName(name));
    }
}
