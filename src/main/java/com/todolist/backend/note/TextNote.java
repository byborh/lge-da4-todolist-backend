package com.todolist.backend.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("TEXT")
public class TextNote extends Note {
    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = false)
    @JsonIgnore
    private ListNote listNote; // Relation inverse

    public TextNote() {
        super(); // Constructeur sans arguments requis par json
    }

    // Constructeur
    public TextNote(ListNote listNote, String title, boolean status, LocalDateTime creationDate) {
        super(listNote, title, null, status, creationDate);

        this.listNote = listNote;
    }


    @Override
    public String getType() {
        return "textnote";
    }

    public ListNote getListNote() {
        return listNote;
    }

    public void setListNote(ListNote listNote) {
        this.listNote = listNote;
    }
}
