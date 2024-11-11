package com.todolist.backend.listnote;

import com.todolist.backend.user.User;
import org.springframework.stereotype.Component;

@Component
public class ListNoteFactory {
    public ListNote createListNote(User user) {
        ListNote listNote = new ListNote();
        listNote.setUser(user);
        return listNote;
    }
}
