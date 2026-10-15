package com.avocadotech.assetsledger.auth;

import com.avocadotech.assetsledger.auth.dto.AuthResponseDto;
import com.avocadotech.assetsledger.auth.dto.LoginRequestDto;
import com.avocadotech.assetsledger.security.AuthenticatedUser;
import com.avocadotech.assetsledger.security.TokenService;
import com.avocadotech.assetsledger.user.UserService;
import com.avocadotech.assetsledger.user.dto.UserRequestDto;
import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.password()));

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return tokenService.issue(user);
    }
}