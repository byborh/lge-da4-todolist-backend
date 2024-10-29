package com.todolist.backend.note.strategy;

import com.todolist.backend.note.TextNote;

public class TextNoteStrategy implements NoteStrategy{
    private final TextNote textNote;

    public TextNoteStrategy(TextNote textNote) {
        this.textNote = textNote;
    }

    @Override
    public boolean validateNote() {
        return textNote.getTitle() != null && textNote.getTitle().isEmpty();
    }

    @Override
    public String formatNote() {
        return "Title: " + textNote.getTitle() + " (Text Note)";
    }
}
