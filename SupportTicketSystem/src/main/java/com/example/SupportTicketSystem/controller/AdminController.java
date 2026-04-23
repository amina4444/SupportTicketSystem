package com.example.SupportTicketSystem.controller;

import com.example.SupportTicketSystem.entity.Role;
import com.example.SupportTicketSystem.entity.User;
import com.example.SupportTicketSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id,
                                           @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateRole(id, role));
    }
}
