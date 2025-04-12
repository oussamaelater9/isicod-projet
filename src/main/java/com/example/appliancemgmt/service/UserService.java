package com.example.appliancemgmt.service;

import com.example.appliancemgmt.entity.Client;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.entity.User.Role;
import com.example.appliancemgmt.repository.ClientRepository;
import com.example.appliancemgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    public User signUp(SignUpRequest signUpRequest) {
        Optional<User> existingUser = userRepository.findByUsername(signUpRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setRole(Role.CONSULTANT);

        Client client = new Client();
        client.setCompanyName(signUpRequest.getCompanyName());
        client.setName(signUpRequest.getName());
        client.setContact(signUpRequest.getContact());
        client.setEmail(signUpRequest.getEmail());
        client.setPhone(signUpRequest.getPhone());
        client.setIndustry(signUpRequest.getIndustry());
        client.setAddress(signUpRequest.getAddress());
        client.setUser(user);

        user.setClient(client);
        return userRepository.save(user);
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (user.getRole() == null) {
            user.setRole(Role.CONSULTANT);
        }
        return userRepository.save(user);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User existingUser = existingUserOpt.get();
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public static class SignUpRequest {
        private String username;
        private String password;
        private String companyName;
        private String name;
        private String contact;
        private String email;
        private String phone;
        private String industry;
        private String address;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContact() { return contact; }
        public void setContact(String contact) { this.contact = contact; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getIndustry() { return industry; }
        public void setIndustry(String industry) { this.industry = industry; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}