package com.example.SupportTicketSystem.service;

import com.example.SupportTicketSystem.entity.Role;
import com.example.SupportTicketSystem.entity.User;
import com.example.SupportTicketSystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_USER);
        return repo.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    public User updateRole(Long id, Role role) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return repo.save(user);
    }
}
