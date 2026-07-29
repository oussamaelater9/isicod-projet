package com.example.appliancemgmt.controller;

import com.example.appliancemgmt.dto.LogResponseDTO;
import com.example.appliancemgmt.dto.SignUpRequest;
import com.example.appliancemgmt.dto.UserDTO;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.service.LogService;
import com.example.appliancemgmt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LogService logService;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setUsername("oussama");
        user.setName("Oussama");
        user.setEmail("oussama@test.com");
        user.setPhone("0600000000");
        user.setAddress("Temara");
        user.setRole(User.Role.ADMIN);
        user.setCreatedAt(LocalDateTime.now());
        user.setClients(new ArrayList<>());

        when(userService.signUp(any(SignUpRequest.class)))
                .thenReturn(user);

        SignUpRequest request = new SignUpRequest();
        request.setUsername("oussama");
        request.setPassword("123456");
        request.setName("Oussama");
        request.setEmail("oussama@test.com");
        request.setPhone("0600000000");
        request.setAddress("Temara");

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.username").value("oussama"));

        ArgumentCaptor<String> actionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> entityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(logService).logAction(
                actionCaptor.capture(),
                entityCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals("CREATE", actionCaptor.getValue());
        assertEquals("User", entityCaptor.getValue());
        assertEquals("Created user with username: oussama", messageCaptor.getValue());
    }

    @Test
    void shouldReturnBadRequestWhenUsernameAlreadyExists() throws Exception {

        when(userService.signUp(any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        SignUpRequest request = new SignUpRequest();
        request.setUsername("oussama");
        request.setPassword("123456");
        request.setName("Oussama");
        request.setEmail("oussama@test.com");
        request.setPhone("0600000000");
        request.setAddress("Temara");

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(logService, never()).logAction(any(), any(), any());
    }

    @Test
    void shouldRejectInvalidRequest() throws Exception {

        SignUpRequest request = new SignUpRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, never()).signUp(any());
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("oussama");
        user.setName("Oussama");
        user.setEmail("oussama@test.com");
        user.setPhone("0600000000");
        user.setAddress("Casablanca");
        user.setRole(User.Role.CONSULTANT);
        user.setCreatedAt(LocalDateTime.now());
        user.setClients(new ArrayList<>());

        when(userService.getUserById(1L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("oussama"));

        verify(userService).getUserById(1L);
    }
    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        when(userService.getUserById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User not found with id: 1"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userService).getUserById(1L);
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {

        UserDTO request = new UserDTO(
                null,
                "newuser",
                "New Name",
                "new@test.com",
                "0600000000",
                "Casablanca",
                null,
                null,
                new ArrayList<>()
        );

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newuser");
        updatedUser.setName("New Name");
        updatedUser.setEmail("new@test.com");
        updatedUser.setPhone("0600000000");
        updatedUser.setAddress("Casablanca");
        updatedUser.setRole(User.Role.CONSULTANT);
        updatedUser.setCreatedAt(LocalDateTime.now());
        updatedUser.setClients(new ArrayList<>());

        when(userService.updateUser(eq(1L), any(UserDTO.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.username").value("newuser"));

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
        verify(logService).logAction(
                "UPDATE",
                "User",
                "Updated user with ID: 1"
        );
    }

    @Test
    void shouldReturnBadRequestWhenUserToUpdateDoesNotExist() throws Exception {

        UserDTO request = new UserDTO(
                null,
                "newuser",
                "New Name",
                "new@test.com",
                "0600000000",
                "Casablanca",
                null,
                null,
                new ArrayList<>()
        );

        when(userService.updateUser(eq(1L), any(UserDTO.class)))
                .thenThrow(new IllegalArgumentException("User with id 1 not found"));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User with id 1 not found"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
        verify(logService, never()).logAction(any(), any(), any());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {

        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userService).deleteUser(1L);
        verify(logService).logAction(
                "DELETE",
                "User",
                "Deleted user with ID: 1"
        );
    }

    @Test
    void shouldReturnBadRequestWhenDeletingNonExistingUser() throws Exception {

        doThrow(new IllegalArgumentException("User with id 1 not found"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User with id 1 not found"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userService).deleteUser(1L);
        verify(logService, never()).logAction(any(), any(), any());
    }
}