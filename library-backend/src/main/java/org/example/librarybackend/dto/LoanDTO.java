package org.example.librarybackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private Long userId;
    private Long bookId;

    @NotNull(message = "Loan date is required")
    private LocalDate loanDate;

    @NotNull(message = "Return due date is required")
    private LocalDate returnDueDate;

    @Pattern(regexp = "issued|returned|overdue", message = "Invalid status")
    private String status;
}

