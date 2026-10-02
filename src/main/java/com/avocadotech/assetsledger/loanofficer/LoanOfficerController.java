package com.avocadotech.assetsledger.loanofficer;


import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/loanofficer")
public class LoanOfficerController {
    LoanOfficerService loanOfficerService;

    public LoanOfficerController(LoanOfficerService loanOfficerService) {
        this.loanOfficerService = loanOfficerService;
    }

    @PostMapping
    public ResponseEntity<LoanOfficerResponseDto> create(@Valid @RequestBody LoanOfficerRequestDto request){
        LoanOfficerResponseDto newLoanOfficerCreated = loanOfficerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLoanOfficerCreated);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanOfficerResponseDto> getById(@PathVariable UUID id){
        LoanOfficerResponseDto loanOfficerById = loanOfficerService.findById(id);
        return ResponseEntity.ok(loanOfficerById);
    }
    @GetMapping()
    public ResponseEntity<List<LoanOfficerResponseDto>> getAll(){
        List<LoanOfficerResponseDto> loanOfficerResponseDtoList = loanOfficerService.findAll();
        return ResponseEntity.ok(loanOfficerResponseDtoList) ;
    }
    @PutMapping("{id}")
    public ResponseEntity<LoanOfficerResponseDto>updateLoanOfficer(@PathVariable UUID id, @RequestBody LoanOfficerUpdateRequestDto request){
        LoanOfficerResponseDto updatedLoanOfficer = loanOfficerService.updateById(id,request);
        return ResponseEntity.ok(updatedLoanOfficer);

    }
}
