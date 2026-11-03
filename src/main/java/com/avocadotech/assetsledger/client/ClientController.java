package com.avocadotech.assetsledger.client;


import com.avocadotech.assetsledger.client.dto.ClientRequestDto;
import com.avocadotech.assetsledger.client.dto.ClientResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto clientRequestDto, @AuthenticationPrincipal Jwt jwt){
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(clientService.createClient(userId,clientRequestDto ));
    }

    @Transactional(readOnly = true)
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponseDto> getClientById(@AuthenticationPrincipal Jwt jwt , @PathVariable UUID clientId){
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(clientService.getById(clientId, userId));
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getMyClients(@AuthenticationPrincipal Jwt jwt){
        UUID userId = UUID.fromString(jwt.getSubject());
        List<ClientResponseDto> listClients = clientService.getMyClients(userId);
        return ResponseEntity.ok(listClients);
    }
}
