package org.example.librarybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibrarianNoteDTO {
    private Long userId;
    private String note;
    private LocalDateTime createdAt;
}
