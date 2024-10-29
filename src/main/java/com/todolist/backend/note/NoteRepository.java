package com.todolist.backend.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // des méthodes spécifiques
    Optional<Note> findById(Long id);

    List<Note> findByTitleContaining(String title);

    void deleteById(Long userId);
}
