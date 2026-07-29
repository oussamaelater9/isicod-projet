package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.SignUpRequest;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSignUpSuccessfully() {
        SignUpRequest request = new SignUpRequest();
      // Arrange
        request.setUsername("oussama");
        request.setPassword("123456");
        request.setName("Oussama");
        request.setEmail("oussama@gmail.com");
        request.setPhone("0600000000");
        request.setAddress("Temara");


        // Mockito
        when(userRepository.findByUsername("oussama"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

// Act
        User savedUser = userService.signUp(request);
        assertNotNull(savedUser);
        assertEquals("oussama", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        User capturedUser = captor.getValue();
        assertEquals("oussama", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        SignUpRequest request = new SignUpRequest();

        request.setUsername("oussama");
        request.setPassword("123456");


        User existingUser = new User();
        existingUser.setUsername("oussama");

        when(userRepository.findByUsername("oussama"))
                .thenReturn(Optional.of(existingUser));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.signUp(request);
        });

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

}