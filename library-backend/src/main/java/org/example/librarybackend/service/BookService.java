package org.example.librarybackend.service;

import org.example.librarybackend.dto.BookDTO;
import org.example.librarybackend.model.Book;
import org.example.librarybackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;


    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO findBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(this::toDTO).orElse(null);
    }

    public boolean addBook(BookDTO bookDTO) {
        if (bookDTO.getTitle().isBlank() || bookDTO.getAuthor().isBlank() ||
                bookDTO.getGenre().isBlank() || bookDTO.getCode().isBlank()) {
            throw new IllegalArgumentException("Required fields cannot be blank");
        }
        try {
            Book book = toEntity(bookDTO);
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateBook(Long id, BookDTO bookDTO) {
        if (bookRepository.existsById(id)) {
            Book book = toEntity(bookDTO);
            book.setId(id);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BookDTO> searchBooks(String title, String author, String genre) {
        return bookRepository.findAll().stream()
                .filter(book -> (title == null || book.getTitle().contains(title)) &&
                        (author == null || book.getAuthor().contains(author)) &&
                        (genre == null || book.getGenre().equalsIgnoreCase(genre)))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convert Book entity to BookDTO
    private BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCode(book.getCode());
        dto.setGenre(book.getGenre());
        dto.setAvailable(book.isAvailable());
        dto.setLoanPeriod(book.getLoanPeriod());
        dto.setNextReturnDate(book.getNextReturnDate());
        dto.setNotificationMessage(book.getNotificationMessage());
        return dto;
    }

    // Convert BookDTO to Book entity
    private Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCode(bookDTO.getCode());
        book.setGenre(bookDTO.getGenre());
        book.setAvailable(bookDTO.isAvailable());
        book.setLoanPeriod(bookDTO.getLoanPeriod());
        book.setNextReturnDate(bookDTO.getNextReturnDate());
        book.setNotificationMessage(bookDTO.getNotificationMessage());
        return book;
    }


}
