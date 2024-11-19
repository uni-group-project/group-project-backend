package org.example.librarybackend.repository;

import org.example.librarybackend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    @Query("SELECT l FROM Loan l WHERE l.returnDueDate < :currentDate AND l.status = 'issued'")
    List<Loan> findOverdueLoans(@Param("currentDate") LocalDate currentDate);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(String status);
}
