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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private static final Set<String> VALID_STATUSES = Set.of("issued", "returned", "overdue");

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
        validateStatus("issued"); // Перевірка статусу
        Optional<User> user = userRepository.findById(loanDTO.getUserId());
        Optional<Book> book = bookRepository.findById(loanDTO.getBookId());

        if (user.isEmpty()) {
            return "User not found.";
        }
        if (book.isEmpty()) {
            return "Book not found.";
        }

        Book bookToLoan = book.get();
        if (!bookToLoan.isAvailable()) {
            return "Book is not available.";
        }

        bookToLoan.setAvailable(false);
        bookToLoan.setNextReturnDate(loanDTO.getReturnDueDate()); // Оновлення дати повернення
        bookRepository.save(bookToLoan);

        Loan loan = new Loan();
        loan.setUser(user.get());
        loan.setBook(bookToLoan);
        loan.setLoanDate(loanDTO.getLoanDate());
        loan.setReturnDueDate(loanDTO.getReturnDueDate());
        loan.setStatus("issued");
        loanRepository.save(loan);

        return "Book issued successfully!";
    }

    public boolean returnLoan(Long loanId) {
        return loanRepository.findById(loanId).map(loan -> {
            validateStatus("returned"); // Перевірка статусу
            loan.setStatus("returned");
            Book book = loan.getBook();
            book.setAvailable(true);
            book.setNextReturnDate(null); // Скидання дати повернення
            bookRepository.save(book);
            loanRepository.save(loan);
            return true;
        }).orElse(false);
    }

    public boolean updateLoanStatus(Long loanId, String status) {
        validateStatus(status); // Перевірка статусу
        return loanRepository.findById(loanId).map(loan -> {
            loan.setStatus(status);
            loanRepository.save(loan);
            return true;
        }).orElse(false);
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public List<LoanDTO> getUserLoans(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        List<Loan> loans = loanRepository.findByUserId(userId);
        if (loans.isEmpty()) {
            throw new RuntimeException("No loans found for this user");
        }

        return loans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(today);
        if (overdueLoans.isEmpty()) {
            throw new RuntimeException("No overdue loans found");
        }
        return overdueLoans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LoanDTO mapToDTO(Loan loan) {
        return new LoanDTO(
                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getLoanDate(),
                loan.getReturnDueDate(),
                loan.getStatus()
        );
    }
}
