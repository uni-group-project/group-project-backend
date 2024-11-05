package org.example.librarybackend.repository;

import org.example.librarybackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
}
