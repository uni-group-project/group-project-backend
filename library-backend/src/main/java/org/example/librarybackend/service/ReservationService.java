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

        if (user.isEmpty()) {
            return "User not found!";
        }

        if (book.isEmpty()) {
            return "Book not found!";
        }

        if (!book.get().isAvailable()) {
            return "Book is not available for reservation!";
        }

        Reservation reservation = Reservation.builder()
                .user(user.get())
                .book(book.get())
                .reservationDate(reservationDTO.getReservationDate())
                .reservationExpiryDate(reservationDTO.getReservationExpiryDate())
                .build();

        reservationRepository.save(reservation);

        // Update book status
        book.get().setAvailable(false);
        bookRepository.save(book.get());

        return "Book reserved successfully!";
    }

    public boolean cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isPresent()) {
            Book book = reservation.get().getBook();
            book.setAvailable(true); // Release the book
            bookRepository.save(book);

            reservationRepository.deleteById(reservationId);
            return true;
        }

        return false;
    }

    public List<ReservationDTO> getUserReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream()
                .map(reservation -> new ReservationDTO(
                        reservation.getUser().getId(),
                        reservation.getBook().getId(),
                        reservation.getReservationDate(),
                        reservation.getReservationExpiryDate()
                ))
                .collect(Collectors.toList());
    }
}
