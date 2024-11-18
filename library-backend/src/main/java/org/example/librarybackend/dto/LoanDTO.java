package org.example.librarybackend.dto;

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
    private LocalDate loanDate;
    private LocalDate returnDueDate;
    private String status; // Наприклад: "issued", "returned", "overdue"


}

