package org.example.librarybackend.controller;

import org.example.librarybackend.dto.LibrarianNoteDTO;
import org.example.librarybackend.service.LibrarianNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/librarian-notes")
public class LibrarianNoteController {

    @Autowired
    private LibrarianNoteService librarianNoteService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> addNoteToUser(@RequestParam Long userId, @RequestBody String note) {
        if (librarianNoteService.addNoteToUser(userId, note)) {
            return ResponseEntity.ok("Note added successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add note");
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<LibrarianNoteDTO>> getNotesForUser(@PathVariable Long userId) {
        if (!librarianNoteService.userExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Користувач не знайдений
        }
        List<LibrarianNoteDTO> notes = librarianNoteService.getNotesForUser(userId);
        if (notes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Примітки не знайдені
        }
        return ResponseEntity.ok(notes);
    }
}
