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

        if (user.isEmpty() || book.isEmpty() || !book.get().isAvailable()) {
            return "Invalid user or book is not available";
        }

        Book bookToLoan = book.get();
        bookToLoan.setAvailable(false);
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
            loan.setStatus("returned");
            Book book = loan.getBook();
            book.setAvailable(true);
            bookRepository.save(book);
            loanRepository.save(loan);
            return true;
        }).orElse(false);
    }

    public List<LoanDTO> getUserLoans(Long userId) {
        return loanRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loanRepository.findOverdueLoans(today)
                .stream()
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
