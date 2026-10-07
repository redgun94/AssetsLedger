package com.avocadotech.assetsledger.user;

import com.avocadotech.assetsledger.user.dto.UserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("create: hashea la contraseña y asigna el rol USER")
    void create_hashesPasswordAndAssignsUserRole() {
        // Arrange
        var request = new UserRequestDto("leo@example.com","Test1234!","Leo", "Martinez","3055551234");
        when(passwordEncoder.encode("Test1234!")).thenReturn("$2a$10$hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        userService.create(request);

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertThat(saved.getPassword()).isEqualTo("$2a$10$hash");
        assertThat(saved.getRole()).isEqualTo(User.Role.USER);
    }
}
