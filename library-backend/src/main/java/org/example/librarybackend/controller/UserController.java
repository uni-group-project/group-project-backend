package org.example.librarybackend.controller;

import org.example.librarybackend.dto.UserAuthResponseDTO;
import org.example.librarybackend.dto.UserLoginDTO;
import org.example.librarybackend.dto.UserRegisterDTO;
import org.example.librarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<UserAuthResponseDTO> login(@RequestBody UserLoginDTO loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserAuthResponseDTO> registerUser(@RequestBody UserRegisterDTO registerDto) {
        return ResponseEntity.ok(userService.register(registerDto));
    }
}
