package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.security.CurrentUser;
import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import com.avocadotech.assetsledger.user.dto.UserUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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
        UUID id = CurrentUser.toId(jwt);
        return ResponseEntity.ok(userService.getMe(id));
    }
    @PutMapping()
    public ResponseEntity<UserResponseDto> updateById(@Valid @RequestBody UserUpdateRequestDto request, @AuthenticationPrincipal Jwt jwt){
        UUID id = CurrentUser.toId(jwt);
        UserResponseDto userUpdated = userService.updateById(request,id);
        return ResponseEntity.ok(userUpdated);
    }
}
