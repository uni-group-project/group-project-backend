package org.example.librarybackend.repository;

import org.example.librarybackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.reservationExpiryDate < :currentDate")
    List<Reservation> findExpiredReservations(@Param("currentDate") LocalDate currentDate);

    List<Reservation> findByBookId(Long bookId);

    List<Reservation> findByBookIdAndReservationExpiryDateAfter(Long bookId, LocalDate expiryDate);
}
