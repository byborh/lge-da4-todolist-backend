package com.todolist.backend.note;

import com.todolist.backend.listnote.ListNote;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NoteFactory {

    public Note createNote(ListNote listNote, String type, String title, String content, boolean status, LocalDateTime creationDate) {
        switch (type.toLowerCase()) {
            case "textnote":
                return new TextNote(listNote, title, status, creationDate);
            case "longtextnote":
                return new LongTextNote(listNote, title, content, status, creationDate);
            default:
                throw new IllegalArgumentException("Type de note inconnu : " + type);
        }
    }

    // à modifier le type aussi, dattebayo
    public Note modifyNote(Note note, String title, String content, boolean status, LocalDateTime creationDate) {
        note.setTitle(title);
        note.setStatus(status);
        note.setCreationDate(creationDate);

        if(note instanceof TextNote) {
            ((TextNote) note).setTitle(title);
        } else if(note instanceof LongTextNote) {
            ((LongTextNote) note).setContent(content);
        } else {
            throw new IllegalArgumentException("Type de note inconnu : " + note.getClass().getSimpleName());
        }
        return note;
    }
}
