package com.example.appliancemgmt.integration;

import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "frontend.url=http://localhost:4200",
        "spring.mail.host=localhost",
        "spring.mail.port=25"
})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JavaMailSender mailSender;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserByIdSuccessfully() throws Exception {

        User user = new User();
        user.setUsername("oussama");
        user.setName("Oussama EL ATER");
        user.setEmail("oussama@test.com");
        user.setPhone("0600000000");
        user.setAddress("Casablanca");
        user.setPassword("password123");
        user.setRole(User.Role.CONSULTANT);

        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("oussama"))
                .andExpect(jsonPath("$.data.name").value("Oussama EL ATER"))
                .andExpect(jsonPath("$.data.email").value("oussama@test.com"));
    }
}