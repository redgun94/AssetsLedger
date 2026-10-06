package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.user.dto.UserRequestDto;
import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import com.avocadotech.assetsledger.user.dto.UserUpdateRequestDto;
import com.avocadotech.assetsledger.user.exception.EmailAlreadyExistsException;
import com.avocadotech.assetsledger.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public List<User> findByPhone(String phone){
        return userRepository.findByPhone(phone);
    }

    public UserResponseDto create(@Valid UserRequestDto request){
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }
        User user =  new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setLastName(request.last_name());
        user.setPhone(request.phone());
        user.setRole(User.Role.ADMIN);
        user.setAccountStatus(User.Account_Status.PENDING_PROFILE);

        User saved = userRepository.save(user);

        return new UserResponseDto(saved.getId(), saved.getName(), saved.getLastName(), saved.getEmail());

    }
    public void delete(UUID id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserResponseDto getById(UUID id){
        return userRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(),user.getName(), user.getLastName(), user.getEmail());
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public UserResponseDto updateById(UserUpdateRequestDto request, UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        toNewUpdatedUser(user,request);
        User saved = userRepository.save(user);
        return toResponseDto(saved);
    }

    private void toNewUpdatedUser(User user, UserUpdateRequestDto updatedUser) {
        user.setName(updatedUser.name());
        user.setLastName(updatedUser.lastName());
        user.setPhone(updatedUser.phone());

    }
}
