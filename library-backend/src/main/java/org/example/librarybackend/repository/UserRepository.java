package org.example.librarybackend.repository;

import org.example.librarybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByCardNumber(String cardNumber);

    @Query("SELECT r.name FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<String> findRolesByUserId(@Param("userId") Long userId);

    boolean existsByCardNumber(String cardNumber);
}
