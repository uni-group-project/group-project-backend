package org.example.librarybackend.controller;

import org.example.librarybackend.dto.ReservationDTO;
import org.example.librarybackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/expired")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<ReservationDTO>> getExpiredReservations() {
        return ResponseEntity.ok(reservationService.getExpiredReservations());
    }
}
