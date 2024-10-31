package com.todolist.backend.note;

import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class LongTextNote implements Note{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = false)
    private ListNote listNote; // Relation inverse


    public LongTextNote(Long id, String title, String content, LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Optional<String> getContent() {
        return Optional.of(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
