package com.todolist.backend.listnote;

import com.todolist.backend.note.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class ListNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();
     */

    @OneToMany(cascade = CascadeType.ALL)
    private List<TextNote> textNotes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<LongTextNote> longTextNotes;


    // Injection de Dépendances
    private final NoteFactory noteFactory;
    private final NoteRepository noteRepository;

    public ListNote(NoteFactory noteFactory, NoteRepository noteRepository) {
        this.noteFactory = noteFactory;
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(String type, String title, Optional<String> content, LocalDateTime creationDate) {
        Note note = noteFactory.createNote(type, title, String.valueOf(content), creationDate);
        notes.add(note);
        noteRepository.save(note);
    }

    public void removeNote(Long noteId) {
        Note noteToRemove = notes.stream()
                .filter(note -> note.getId().equals((noteId)))
                .findFirst()
                .orElse(null);

        if(noteToRemove != null) {
            notes.remove(noteToRemove);
            noteRepository.deleteById(noteId);
        }
    }
}
