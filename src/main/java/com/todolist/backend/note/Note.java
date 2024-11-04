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
    private boolean status;

    private LocalDateTime creationDate;

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

    public abstract String getType();
}
