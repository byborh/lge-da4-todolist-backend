package com.todolist.backend.note;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "note_type")
public abstract class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content; // Le contenu peut être optionnel

    private LocalDateTime creationDate;

    // Constructeur protégé pour permettre l'instanciation par des sous-classes
    protected Note(String title, LocalDateTime creationDate) {
    }

    public Note(String title, String content, LocalDateTime creationDate) {
        this.title = title;
        this.content = content;
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

    public abstract String getType();
}
