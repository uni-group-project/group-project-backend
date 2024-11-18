package org.example.librarybackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "librarian_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibrarianNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
