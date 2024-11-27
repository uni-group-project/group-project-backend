package org.example.librarybackend.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.example.librarybackend.dto.BookDTO;
import org.example.librarybackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    @PermitAll
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        BookDTO book = bookService.findBook(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) {
        // Перевірка обов'язкових полів
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty() ||
                bookDTO.getAuthor() == null || bookDTO.getAuthor().trim().isEmpty() ||
                bookDTO.getCode() == null || bookDTO.getCode().trim().isEmpty() ||
                bookDTO.getGenre() == null || bookDTO.getGenre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Title, author, code, and genre must not be empty");
        }

        // Виклик сервісу для додавання книги
        if (bookService.addBook(bookDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add book");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        if (bookService.updateBook(id, bookDTO)) {
            return ResponseEntity.ok("Book updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update book");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok("Book deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    @GetMapping("/search")
    @PermitAll
    public ResponseEntity<List<BookDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        List<BookDTO> results = bookService.searchBooks(title, author, genre);
        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }
}
