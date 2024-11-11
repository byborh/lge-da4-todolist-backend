package com.todolist.backend.listnote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ListNoteRepository extends JpaRepository<ListNote, Long> {
    Optional<ListNote> findById(Long id);
    void deleteById(Long id);
}
