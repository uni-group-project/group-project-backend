package org.example.librarybackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String genre;

    @Column(nullable = false)
    private boolean available;

    @Column(name = "loan_period")
    private Integer loanPeriod;

    @Column(name = "next_return_date")
    private LocalDate nextReturnDate;

    @Column(name = "notification_message")
    private String notificationMessage;
}