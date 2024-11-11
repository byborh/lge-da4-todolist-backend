package com.todolist.backend.listnote;

import com.todolist.backend.note.LongTextNote;
import com.todolist.backend.note.Note;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    @PostMapping("/{userId}/{listId}")
    public ResponseEntity<ListNote> addNote(@PathVariable Long userId, @PathVariable Long listId, @RequestBody Note note) {
        String type = note.getClass().getSimpleName();
        String title = note.getTitle();
        String content = note instanceof LongTextNote ? note.getContent().orElse(null) : null;
        LocalDateTime creationDate = note.getCreationDate();

        // je teste le type de la note car là, je comprends pas pq ça ne fonctionne pas :
        System.out.println(type);

        // Associer l'utilisateur à la liste de notes, puis ajouter la note
        ListNote updatedListNote = listNoteService.addNote(listId, userId, type, title, Optional.ofNullable(content), creationDate);
        return updatedListNote != null ? ResponseEntity.ok(updatedListNote) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ListNote> addListNote(@PathVariable Long userId) {
        try {
            ListNote listNote = listNoteService.addListNote(userId);
            return ResponseEntity.ok(listNote);
        } catch (EntityNotFoundException err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Récupérer toutes les notes d'une liste spécifique
    @GetMapping("/{userId}/{listId}")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable Long userId, @PathVariable Long listId) {
        List<Note> notes = listNoteService.getAllNotes(userId, listId);
        return ResponseEntity.ok(notes);
    }

    // Récupérer une note spécifique par son ID
    @GetMapping("/{userId}/{listId}/{noteId}")
    public ResponseEntity<Optional<Note>> findNoteById(@PathVariable Long userId, @PathVariable Long listId, @PathVariable Long noteId) {
        Optional<Note> note = listNoteService.findNoteById(userId, listId, noteId);
        return note.isPresent() ? ResponseEntity.ok(note) : ResponseEntity.notFound().build();
    }

    // Modifier une note dans une liste spécifique
    @PutMapping("/{userId}/{listId}/{noteId}")
    public ResponseEntity<ListNote> modifyNote(
            @PathVariable Long userId,
            @PathVariable Long listId,
            @RequestBody Note noteDetails) {

        Optional<ListNote> updatedListNote = Optional.ofNullable(listNoteService.modifyNote(userId, listId, noteDetails));

        return updatedListNote
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Supprimer une note spécifique dans une liste
    @DeleteMapping("/{userId}/{listId}/{noteId}")
    public ResponseEntity<Void> removeNote(@PathVariable Long userId, @PathVariable Long listId, @PathVariable Long noteId) {
        boolean removed = listNoteService.removeNote(userId, listId, noteId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}/{listId}")
    public ResponseEntity<Void> removeList(@PathVariable Long userId, @PathVariable Long listId) {
        boolean removed = listNoteService.removeList(userId, listId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
