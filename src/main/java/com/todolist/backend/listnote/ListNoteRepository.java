package com.todolist.backend.listnote;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ListNoteRepository extends JpaRepository<ListNote, Long> {
}
