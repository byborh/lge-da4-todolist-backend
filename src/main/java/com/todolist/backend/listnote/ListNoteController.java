package com.todolist.backend.listnote;

import com.todolist.backend.note.LongTextNote;
import com.todolist.backend.note.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/listnote")
public class ListNoteController {
    private final ListNoteService listNoteService;

    @Autowired
    public ListNoteController(ListNoteService listNoteService) {
        this.listNoteService = listNoteService;
    }


    // create a new note
    @PostMapping("/note/{listId}")
    public ResponseEntity<ListNote> addNote(@PathVariable Long listId, @RequestBody Note note) {
        String type = note.getClass().getSimpleName();
        String title = note.getTitle();
        String content = note instanceof LongTextNote ? String.valueOf(((LongTextNote) note).getContent()) : null;
        LocalDateTime creationDate = note.getCreationDate();

        ListNote updatedListNote = listNoteService.addNote(listId, type, title, Optional.ofNullable(content), creationDate);
        return updatedListNote != null ? ResponseEntity.ok(updatedListNote) : ResponseEntity.notFound().build();
    }

    // get all notes of user
    @GetMapping("/{listId}")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable Long listId) {
        List<Note> notes = listNoteService.getAllNotes(listId);
        return ResponseEntity.ok(notes);
    }

    // get a specific note
    @GetMapping("/note/{noteId}")
    public ResponseEntity<Optional<Note>> findNoteById(@PathVariable Long noteId) {
        Optional<Note> note = listNoteService.findNoteById(noteId);
        return note.isPresent() ? ResponseEntity.ok(note) : ResponseEntity.notFound().build();
    }

    // delete a specific note
    @DeleteMapping("/{listId}/note/{noteId}")
    public ResponseEntity<Void> removeNote(@PathVariable Long listId, @PathVariable Long noteId) {
        boolean removed = listNoteService.removeNote(listId, noteId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
