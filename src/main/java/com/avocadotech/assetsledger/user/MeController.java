package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserService userService;

    public MeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal Jwt jwt){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(userService.getMe(id));
    }
}
