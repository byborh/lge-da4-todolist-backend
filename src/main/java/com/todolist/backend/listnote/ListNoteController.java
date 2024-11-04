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

    // Créer une nouvelle note dans une liste spécifique
    @PostMapping("/{listId}/notes")
    public ResponseEntity<ListNote> addNote(@PathVariable Long listId, @RequestBody Note note) {
        String type = note.getClass().getSimpleName();
        String title = note.getTitle();
        String content = note instanceof LongTextNote ? note.getContent().orElse(null) : null;
        LocalDateTime creationDate = note.getCreationDate();

        ListNote updatedListNote = listNoteService.addNote(listId, type, title, Optional.ofNullable(content), creationDate);
        return updatedListNote != null ? ResponseEntity.ok(updatedListNote) : ResponseEntity.notFound().build();
    }

    // Récupérer toutes les notes d'une liste spécifique
    @GetMapping("/{listId}/notes")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable Long listId) {
        List<Note> notes = listNoteService.getAllNotes(listId);
        return ResponseEntity.ok(notes);
    }

    // Récupérer une note spécifique par son ID
    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Optional<Note>> findNoteById(@PathVariable Long noteId) {
        Optional<Note> note = listNoteService.findNoteById(noteId);
        return note.isPresent() ? ResponseEntity.ok(note) : ResponseEntity.notFound().build();
    }

    // Modifier une note dans une liste spécifique
    @PutMapping("/{listId}/notes")
    public ResponseEntity<ListNote> modifyNote(
            @PathVariable Long listId,
            @RequestBody Note noteDetails) {

        Optional<ListNote> updatedListNote = Optional.ofNullable(listNoteService.modifyNote(listId, noteDetails));

        return updatedListNote
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Supprimer une note spécifique dans une liste
    @DeleteMapping("/{listId}/notes/{noteId}")
    public ResponseEntity<Void> removeNote(@PathVariable Long listId, @PathVariable Long noteId) {
        boolean removed = listNoteService.removeNote(listId, noteId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
