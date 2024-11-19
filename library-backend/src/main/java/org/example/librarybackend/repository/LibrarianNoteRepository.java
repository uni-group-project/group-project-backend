package org.example.librarybackend.repository;

import org.example.librarybackend.model.LibrarianNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrarianNoteRepository extends JpaRepository<LibrarianNote, Long> {

    @Query("SELECT ln FROM LibrarianNote ln WHERE ln.user.id = :userId")
    List<LibrarianNote> findByUserId(@Param("userId") Long userId);
}
