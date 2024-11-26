package org.example.librarybackend.service;

import org.example.librarybackend.dto.ReservationDTO;
import org.example.librarybackend.model.Book;
import org.example.librarybackend.model.Reservation;
import org.example.librarybackend.model.User;
import org.example.librarybackend.repository.BookRepository;
import org.example.librarybackend.repository.ReservationRepository;
import org.example.librarybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public String reserveBook(ReservationDTO reservationDTO) {
        Optional<User> user = userRepository.findById(reservationDTO.getUserId());
        Optional<Book> book = bookRepository.findById(reservationDTO.getBookId());

        if (user.isEmpty() || book.isEmpty() || !book.get().isAvailable()) {
            return "Invalid user or book is not available";
        }

        Book reservedBook = book.get();
        reservedBook.setAvailable(false);
        bookRepository.save(reservedBook);

        Reservation reservation = new Reservation();
        reservation.setUser(user.get());
        reservation.setBook(reservedBook);
        reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setReservationExpiryDate(reservationDTO.getReservationExpiryDate());
        reservationRepository.save(reservation);

        return "Book reserved successfully!";
    }

    public boolean cancelReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            Book book = reservation.getBook();
            book.setAvailable(true);
            bookRepository.save(book);
            reservationRepository.delete(reservation);
            return true;
        }).orElse(false);
    }

    public List<ReservationDTO> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId); // Перевіряємо існування користувача
    }

    public List<ReservationDTO> getExpiredReservations() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findExpiredReservations(today)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO mapToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getUser().getId(),
                reservation.getBook().getId(),
                reservation.getReservationDate(),
                reservation.getReservationExpiryDate()
        );
    }
}
