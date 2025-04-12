package com.example.appliancemgmt.repository;


import com.example.appliancemgmt.entity.Role;
import com.example.appliancemgmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(User.Role role);
    boolean existsByUsername(String username);
}