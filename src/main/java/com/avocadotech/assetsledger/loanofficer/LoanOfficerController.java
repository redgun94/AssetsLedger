package com.avocadotech.assetsledger.loanofficer;

import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerRequestDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerResponseDto;
import com.avocadotech.assetsledger.loanofficer.dto.LoanOfficerUpdateRequestDto;
import com.avocadotech.assetsledger.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/me/loan-officer")
public class LoanOfficerController {

    private final LoanOfficerService loanOfficerService;

    public LoanOfficerController(LoanOfficerService loanOfficerService) {
        this.loanOfficerService = loanOfficerService;
    }

    @PostMapping
    public ResponseEntity<LoanOfficerResponseDto> create(@AuthenticationPrincipal Jwt jwt,
                                                         @Valid @RequestBody LoanOfficerRequestDto request) {
        LoanOfficerResponseDto created = loanOfficerService.createForUser(CurrentUser.toId(jwt), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<LoanOfficerResponseDto> getMine(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(loanOfficerService.getByUserId(CurrentUser.toId(jwt)));
    }

    @PutMapping
    public ResponseEntity<LoanOfficerResponseDto> updateMine(@AuthenticationPrincipal Jwt jwt,
                                                             @Valid @RequestBody LoanOfficerUpdateRequestDto request) {
        return ResponseEntity.ok(loanOfficerService.updateByUserId(CurrentUser.toId(jwt), request));
    }

/*    private UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }*/
}