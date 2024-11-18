package org.example.librarybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long userId;
    private Long bookId;
    private LocalDate reservationDate;
    private LocalDate reservationExpiryDate;
}
