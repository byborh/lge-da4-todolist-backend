package com.todolist.backend.note;

import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class TextNote implements Note{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = false)
    private ListNote listNote; // Relation inverse

    public TextNote(Long id, String title, LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Optional<String> getContent() {
        return Optional.empty();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
