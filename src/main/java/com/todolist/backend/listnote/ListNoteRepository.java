package com.todolist.backend.listnote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// ça veut dire quoi ça : JpaRepository<ListNote, Long>
public interface ListNoteRepository extends JpaRepository<ListNote, Long> {
    Optional<ListNote> findByUserId(Long userId);
}
