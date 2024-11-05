package com.todolist.backend.note;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// identifier le type de Note pour les json dans la requête en gros
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "note_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextNote.class, name = "TEXT"),
        @JsonSubTypes.Type(value = LongTextNote.class, name = "LONGTEXT")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "note_type")
public abstract class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content; // Le contenu peut être optionnel
    private boolean status;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = true) // peut être null si la note n'est pas encore associée
    private ListNote listNote;

    protected Note() {
        // constructeur sans arguments requis pour hibernates on dirait groooos
    }

    protected Note(String title, String content, boolean status, LocalDateTime creationDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return Optional.ofNullable(content);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean getStatus(){return status;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isStatus() {
        return status;
    }

    public ListNote getListNote() {
        return listNote;
    }

    public void setListNote(ListNote listNote) {
        this.listNote = listNote;
    }

    public abstract String getType();
}
