package org.example.librarybackend.service;

import org.example.librarybackend.dto.LibrarianNoteDTO;
import org.example.librarybackend.model.LibrarianNote;
import org.example.librarybackend.model.User;
import org.example.librarybackend.repository.LibrarianNoteRepository;
import org.example.librarybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibrarianNoteService {

    @Autowired
    private LibrarianNoteRepository librarianNoteRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean addNoteToUser(Long userId, String note) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            LibrarianNote librarianNote = LibrarianNote.builder()
                    .user(user.get())
                    .note(note)
                    .createdAt(LocalDateTime.now())
                    .build();
            librarianNoteRepository.save(librarianNote);
            return true;
        }
        return false;
    }

    public List<LibrarianNoteDTO> getNotesForUser(Long userId) {
        return librarianNoteRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId); // Перевіряємо існування користувача
    }

    private LibrarianNoteDTO mapToDTO(LibrarianNote note) {
        return new LibrarianNoteDTO(
                note.getUser().getId(),
                note.getNote(),
                note.getCreatedAt()
        );
    }
}
