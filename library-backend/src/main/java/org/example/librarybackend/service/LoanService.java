package org.example.librarybackend.service;

import org.example.librarybackend.dto.LoanDTO;
import org.example.librarybackend.model.Book;
import org.example.librarybackend.model.Loan;
import org.example.librarybackend.model.User;
import org.example.librarybackend.repository.BookRepository;
import org.example.librarybackend.repository.LoanRepository;
import org.example.librarybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public String issueLoan(LoanDTO loanDTO) {
        Optional<User> user = userRepository.findById(loanDTO.getUserId());
        Optional<Book> book = bookRepository.findById(loanDTO.getBookId());

        if (user.isEmpty()) {
            return "User not found!";
        }

        if (book.isEmpty()) {
            return "Book not found!";
        }

        if (!book.get().isAvailable()) {
            return "Book is not available!";
        }

        Loan loan = Loan.builder()
                .user(user.get())
                .book(book.get())
                .loanDate(loanDTO.getLoanDate())
                .returnDueDate(loanDTO.getReturnDueDate())
                .status("issued")
                .build();

        loanRepository.save(loan);

        // Update book status
        book.get().setAvailable(false);
        bookRepository.save(book.get());

        return "Book issued successfully!";
    }

    public boolean returnLoan(Long loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);

        if (loan.isPresent()) {
            Loan existingLoan = loan.get();
            existingLoan.setStatus("returned");

            Book book = existingLoan.getBook();
            book.setAvailable(true); // Mark book as available
            bookRepository.save(book);

            loanRepository.save(existingLoan);
            return true;
        }

        return false;
    }

    public List<LoanDTO> getUserLoans(Long userId) {
        List<Loan> loans = loanRepository.findByUserId(userId);
        return loans.stream()
                .map(loan -> new LoanDTO(
                        loan.getUser().getId(),
                        loan.getBook().getId(),
                        loan.getLoanDate(),
                        loan.getReturnDueDate(),
                        loan.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
