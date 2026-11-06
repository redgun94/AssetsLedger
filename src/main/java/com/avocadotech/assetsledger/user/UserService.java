package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.common.exception.BusinessRuleException;
import com.avocadotech.assetsledger.common.exception.InvalidSessionException;
import com.avocadotech.assetsledger.loanofficer.LoanOfficerRepository;
import com.avocadotech.assetsledger.user.dto.UserRequestDto;
import com.avocadotech.assetsledger.user.dto.UserResponseDto;
import com.avocadotech.assetsledger.user.dto.UserUpdateRequestDto;
import com.avocadotech.assetsledger.user.exception.EmailAlreadyExistsException;
import com.avocadotech.assetsledger.user.exception.UserAlreadyDeletedException;
import com.avocadotech.assetsledger.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoanOfficerRepository loanOfficerRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoanOfficerRepository loanOfficerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loanOfficerRepository = loanOfficerRepository;
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
        String email = request.email().toLowerCase();
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException(email);
        }
        User user =  new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setRole(User.Role.USER);
        user.setAccountStatus(User.Account_Status.PENDING_PROFILE);

        User saved = userRepository.save(user);

        return new UserResponseDto(saved.getId(), saved.getName(), saved.getLastName(), saved.getEmail());

    }
    @Transactional
    public void softDelete(UUID id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        if(user.getAccountStatus() == User.Account_Status.DELETED){
            throw new UserAlreadyDeletedException(id);
        }
        user.setDeletedEmail(user.getEmail());
        user.setEmail(id + "@deleted.invalid");
        user.setDeletedAt(LocalDateTime.now());
        user.setAccountStatus(User.Account_Status.DELETED);
        loanOfficerRepository.findByUserId(id).ifPresent(profile -> {
            profile.setDeletedNmls(profile.getNmls());
            profile.setNmls(null);
        });
    }

    public UserResponseDto getById(UUID id){
        return toResponseDto(findNotDeleted(id));
    }

    private User findNotDeleted(UUID id){
        return userRepository.findById(id)
                .filter(user -> user.getAccountStatus() != User.Account_Status.DELETED)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(),user.getName(), user.getLastName(), user.getEmail());
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findByAccountStatusNot(User.Account_Status.DELETED).stream().map(this::toResponseDto).toList();
    }

    public UserResponseDto updateById(UserUpdateRequestDto request, UUID id) {
        User user = findNotDeleted(id);
        toNewUpdatedUser(user,request);
        User saved = userRepository.save(user);
        return toResponseDto(saved);
    }

    private void toNewUpdatedUser(User user, UserUpdateRequestDto updatedUser) {
        user.setName(updatedUser.name());
        user.setLastName(updatedUser.lastName());
        user.setPhone(updatedUser.phone());

    }

    public UserResponseDto getMe(UUID id){

        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        if(user.getAccountStatus() == User.Account_Status.DELETED){
            throw new InvalidSessionException();
        }

        if(user.getAccountStatus() == User.Account_Status.SUSPENDED){
            throw new InvalidSessionException();
        }
        return toResponseDto(user);
    }
}
