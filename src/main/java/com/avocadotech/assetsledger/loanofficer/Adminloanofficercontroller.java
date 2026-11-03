package com.avocadotech.assetsledger.loanofficer;


import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/loan-officers")
public class Adminloanofficercontroller {

    private final LoanOfficerService loanOfficerService;

    public Adminloanofficercontroller(LoanOfficerService loanOfficerService) {
        this.loanOfficerService = loanOfficerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanOfficerResponseDto>getById(@PathVariable UUID id){
        return ResponseEntity.ok(loanOfficerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanOfficerResponseDto>> getAll(){
        return ResponseEntity.ok(loanOfficerService.findAll());
    }
}
