package com.todolist.backend.listnote;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "listNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextNote> textNotes = new ArrayList<>();

    @OneToMany(mappedBy = "listNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LongTextNote> longTextNotes = new ArrayList<>();

    @Transient // Empêche Hibernate d'essayer de mapper ce champ en base de données
    private final NoteFactory noteFactory;

    public ListNote(NoteFactory noteFactory) {
        this.noteFactory = noteFactory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Note> getNotes() {
        // retourne tout type de note
        List<Note> allNotes = new ArrayList<>(textNotes);
        allNotes.addAll(longTextNotes);
        return allNotes;
    }

    /*public void setNotes(List<Note> notes) {
            this.notes = notes;
    }*/

    public void addNote(String type, String title, Optional<String> content, LocalDateTime creationDate) {
        // Utilisation de la fabrique pour créer la note selon le type
        Note note = noteFactory.createNote(type, title, content.orElse(null), creationDate);

        // JE SAIS QUE C'EST CONTRE LA PRINCIPE DE OPEN CLOSED PRINCIPE MAIS BON, C'EST COMME CA, FLEMME DE REFAIRE LE CODE, Y'A UN PORNLEME ????
        if(note instanceof TextNote) {
            textNotes.add((TextNote) note);
        } else if(note instanceof LongTextNote) {
            longTextNotes.add((LongTextNote) note);
        } else {
            throw new IllegalArgumentException("Type de note inconnu : " + note);
        }
    }

    public boolean removeNoteById(Long noteId) {
        // Supprimer la note selon l'ID en cherchant dans les deux collections
        boolean removedFromTextNote = textNotes.removeIf(note -> note.getId().equals(noteId));
        boolean  removedFromLongTextNote = longTextNotes.removeIf(note -> note.getId().equals(noteId));
        return removedFromTextNote || removedFromLongTextNote;
    }
}
