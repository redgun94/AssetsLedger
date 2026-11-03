package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.user.dto.UserRequestDto;
import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import com.avocadotech.assetsledger.user.dto.UserUpdateRequestDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me")
public class UserController {

    UserService userService;
    UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto request){
        UserResponseDto newUserCreated = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto>getById(@PathVariable UUID id){
                UserResponseDto userById = userService.getById(id);
        return ResponseEntity.ok(userById);
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>>getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
        public ResponseEntity<UserResponseDto> updateById(@RequestBody UserUpdateRequestDto request, @PathVariable UUID id){
            UserResponseDto userUpdated = userService.updateById(request,id);
            return ResponseEntity.ok(userUpdated);
    }

}
