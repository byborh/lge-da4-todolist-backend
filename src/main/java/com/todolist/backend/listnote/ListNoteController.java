package com.todolist.backend.listnote;

import com.todolist.backend.note.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/notes")
public class ListNoteController {
    private final ListNoteService listNoteService;

    @Autowired
    public ListNoteController(ListNoteService listNoteService) {
        this.listNoteService = listNoteService;
    }

    // getAllNotes
    @GetMapping("/{listId}/notes")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable Long listId) {
        List<Note> notes = listNoteService.getAllNotes(listId);
        return ResponseEntity.ok(notes);
    }

    // findNoteById
    @GetMapping("/note/{noteId}")
    public ResponseEntity<Optional<Note>> findNoteById(@PathVariable Long noteId) {
        Optional<Note> note = listNoteService.findNoteById(noteId);
        return note.isPresent() ? ResponseEntity.ok(note) : ResponseEntity.notFound().build();
    }

    // addNote
    @PostMapping("/{listId}/note")
    public ResponseEntity<ListNote> addNote(@PathVariable Long listId, @RequestBody Note note) {
        ListNote updatedListNote = listNoteService.addNote(listId, note);
        return ResponseEntity.ok(updatedListNote);
    }

    // removeNote
    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<Void> removeNote(@PathVariable Long noteId) {
        boolean removed = listNoteService.removeNote(noteId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
