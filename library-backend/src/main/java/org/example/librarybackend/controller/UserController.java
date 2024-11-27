package org.example.librarybackend.controller;

import org.example.librarybackend.dto.*;
import org.example.librarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDto) {
        try{
            UserAuthResponseDTO u = userService.login(loginDto);
            if(u == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is blocked or expired");
            }
            return ResponseEntity.ok(u);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data incorrect: " + ex.getMessage());
        }
    }

    @PostMapping("/register/reader")
    public ResponseEntity<?> registerReader(@RequestBody ReaderRegisterDTO registerDto) {
        try{
            return ResponseEntity.ok(userService.registerReader(registerDto));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PostMapping("/register/librarian")
    public ResponseEntity<?> registerLibrarian(@RequestBody LibrarianRegisterDTO registerDto) {
        try{
            return ResponseEntity.ok(userService.registerLibrarian(registerDto));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/block/{readerId}")
    public ResponseEntity<String> blockReader(@PathVariable Long readerId) {
        try{
            userService.blockReader(readerId);
            return ResponseEntity.ok("Reader has been blocked.");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/reader/{cardNumber}")
    public ResponseEntity<?> getReaderByCardNumber(@PathVariable String cardNumber) {
        try {
            return ResponseEntity.ok(userService.getReaderByCardNumber(cardNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
