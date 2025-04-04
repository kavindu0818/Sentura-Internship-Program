package com.example.Sentura_Internship_Program.controller;

import com.example.Sentura_Internship_Program.dto.UserDto;
import com.example.Sentura_Internship_Program.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto user) {
        try {
            String result = service.createUser(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @GetMapping
    public ResponseEntity<String> listUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(service.listUsers(page, pageSize));
        } catch (IOException e) {
            return handleError(e);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserDetails(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(service.getUserDetails(userId));
        } catch (IOException e) {
            return handleError(e);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody String updateData) {
        try {
            return ResponseEntity.ok(service.updateUser(userId, updateData));
        } catch (IOException e) {
            return handleError(e);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(service.deleteUser(userId));
        } catch (IOException e) {
            return handleError(e);
        }
    }

    private ResponseEntity<String> handleError(Exception e) {
        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
    }
}
