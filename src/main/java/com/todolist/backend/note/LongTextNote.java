package com.todolist.backend.note;

import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("LONG_TEXT")
public class LongTextNote extends Note {

    @ManyToOne
    @JoinColumn(name = "list_note_id", nullable = false)
    private ListNote listNote; // Relation inverse

    // Constructeur
    public LongTextNote(String title, String content, LocalDateTime creationDate) {
        super(title, content, creationDate); // Appelle le constructeur parent
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
