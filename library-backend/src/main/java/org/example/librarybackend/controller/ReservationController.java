package org.example.librarybackend.controller;

import org.example.librarybackend.dto.ReservationDTO;
import org.example.librarybackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<String> reserveBook(@RequestBody ReservationDTO reservationDTO) {
        String result = reservationService.reserveBook(reservationDTO);
        if (result.equals("Book reserved successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/cancel/{reservationId}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        boolean result = reservationService.cancelReservation(reservationId);
        if (result) {
            return ResponseEntity.ok("Reservation canceled successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel reservation.");
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<List<ReservationDTO>> getReservationsForUser(@PathVariable Long userId) {
        if (!reservationService.userExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Користувач не знайдений
        }
        List<ReservationDTO> reservations = reservationService.getUserReservations(userId);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Бронювання не знайдені
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/expired")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> getExpiredReservations() {
        List<ReservationDTO> expiredReservations = reservationService.getExpiredReservations();

        if (expiredReservations == null || expiredReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expired reservations found");
        }

        return ResponseEntity.ok(expiredReservations);
    }
}
