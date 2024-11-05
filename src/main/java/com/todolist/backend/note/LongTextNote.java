package com.todolist.backend.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("LONG_TEXT")
public class LongTextNote extends Note {

    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = false)
    @JsonIgnore
    private ListNote listNote; // Relation inverse

    public LongTextNote(){
        // Constructeur sans arguments requis par json
    }

    // Constructeur
    public LongTextNote(String title, String content, boolean status, LocalDateTime creationDate) {
        super(title, content, status, creationDate);
    }

    @Override
    public String getType() {
        return "longtextnote";
    }

    public ListNote getListNote() {
        return listNote;
    }

    public void setListNote(ListNote listNote) {
        this.listNote = listNote;
    }
}
