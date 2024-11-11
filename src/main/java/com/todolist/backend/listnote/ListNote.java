package com.todolist.backend.listnote;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.todolist.backend.note.*;
import com.todolist.backend.user.User;
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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "listNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();
//    @OneToMany(mappedBy = "listNote", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Note> notes;


    @Transient // Empêche Hibernate d'essayer de mapper ce champ en base de données
    private NoteFactory noteFactory;

    // Constructeur par défaut requis pour Jackson
    public ListNote() {}

    // Constructeur principal
    public ListNote(NoteFactory noteFactory) {
        this.noteFactory = noteFactory;
    }

    public Long getId() {
        return this.id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Note> getNotes() {
        List<Note> allNotes = new ArrayList<>(notes);
        // allNotes.addAll(longTextNotes);
        return allNotes;
    }

    public void addNote(Note note) {
        notes.add(note);
        note.setListNote(this);
    }

    public boolean removeNoteById(Long noteId) {
        boolean removedFromNote = notes.removeIf(note -> note.getId().equals(noteId));
        return removedFromNote;
    }

    // Setter pour l'injection de noteFactory si nécessaire
    public void setNoteFactory(NoteFactory noteFactory) {
        this.noteFactory = noteFactory;
    }
}
