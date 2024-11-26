package org.example.librarybackend.controller;

import org.example.librarybackend.dto.LoanDTO;
import org.example.librarybackend.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.");
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('READER') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getLoansForUser(@PathVariable Long userId) {
        try {
            List<LoanDTO> loans = loanService.getUserLoans(userId);
            return ResponseEntity.ok(loans);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> getOverdueLoans() {
        try {
            List<LoanDTO> overdueLoans = loanService.getOverdueLoans();
            return ResponseEntity.ok(overdueLoans);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{loanId}/status")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> updateLoanStatus(@PathVariable Long loanId, @RequestParam String status) {
        try {
            boolean updated = loanService.updateLoanStatus(loanId, status);
            if (updated) {
                return ResponseEntity.ok("Loan status updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
