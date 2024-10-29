package com.todolist.backend.note.strategy;

import com.todolist.backend.note.LongTextNote;

public class LongTextNoteStrategy implements NoteStrategy{
    private final LongTextNote longTextNote;

    public LongTextNoteStrategy(LongTextNote longTextNote) {
        this.longTextNote = longTextNote;
    }

    @Override
    public boolean validateNote() {
        return longTextNote.getTitle() != null && longTextNote.getTitle().isEmpty()
                && longTextNote.getContent() != null && longTextNote.getContent().isEmpty();
    }

    @Override
    public String formatNote() {
        return "Title :" + longTextNote + "\nContent : " + longTextNote.getContent() + " (Long Text Note).";
    }
}
