package com.todolist.backend.note;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Note {
    Long getId();

    String getTitle();

    Optional<String> getContent();

    LocalDateTime getCreationDate();
}
