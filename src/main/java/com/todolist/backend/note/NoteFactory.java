package com.todolist.backend.note;

import java.time.LocalDateTime;

public class NoteFactory {

    public Note createNote(String type, String title, String content, LocalDateTime creationDate) {
        switch (type.toLowerCase()) {
            case "textnote":
                return new TextNote(null, title, creationDate);
            case "longtextnote":
                return new LongTextNote(null, title, content, creationDate);
            default:
                throw new IllegalArgumentException("Type de note inconnu : " + type);
        }
    }
}
