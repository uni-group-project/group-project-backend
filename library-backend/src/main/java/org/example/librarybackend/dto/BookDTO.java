package org.example.librarybackend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String code;
    private String genre;
    private boolean available;
    private Integer loanPeriod;
    private LocalDate nextReturnDate;
    private String notificationMessage;
}
