package org.example.librarybackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Genre is required")
    private String genre;

    private boolean available;

    private Integer loanPeriod;

    private LocalDate nextReturnDate;

    private String notificationMessage;
}
