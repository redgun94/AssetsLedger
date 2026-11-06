package com.avocadotech.assetsledger.user;


import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import com.avocadotech.assetsledger.user.dto.UserUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>>getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateById(@Valid @RequestBody UserUpdateRequestDto request, @PathVariable UUID id){
        UserResponseDto userUpdated = userService.updateById(request,id);
        return ResponseEntity.ok(userUpdated);
    }
}
