package org.example.librarybackend.controller;

import org.example.librarybackend.dto.LoanDTO;
import org.example.librarybackend.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/issue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> issueLoan(@RequestBody LoanDTO loanDTO) {
        String result = loanService.issueLoan(loanDTO);
        if (result.equals("Book issued successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/return/{loanId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> returnBook(@PathVariable Long loanId) {
        boolean result = loanService.returnLoan(loanId);
        if (result) {
            return ResponseEntity.ok("Book returned successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to return book.");
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('READER') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<LoanDTO>> getLoansForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getUserLoans(userId));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<LoanDTO>> getOverdueLoans() {
        return ResponseEntity.ok(loanService.getOverdueLoans());
    }
}
