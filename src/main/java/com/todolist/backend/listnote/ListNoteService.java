package com.todolist.backend.listnote;

import com.todolist.backend.note.Note;
import com.todolist.backend.note.NoteFactory;
import com.todolist.backend.note.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class ListNoteService {
    private final ListNoteRepository listNoteRepository;
    private final NoteRepository noteRepository;
    private final NoteFactory noteFactory;

    @Autowired
    public ListNoteService(ListNoteRepository listNoteRepository, NoteRepository noteRepository, NoteFactory noteFactory) {
        this.listNoteRepository = listNoteRepository;
        this.noteRepository = noteRepository;
        this.noteFactory = noteFactory;
    }

    public ListNote addNote(Long listId, Long userId, String type, String title, Optional<String> content, LocalDateTime creationDate) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        if (optionalListNote.isPresent()) {
            ListNote listNote = optionalListNote.get();

            // Créer la note sans besoin de la connaissance de la liste
            Note note = noteFactory.createNote(type, title, content.orElse(null), false, creationDate);

            // Ajouter la note à la liste de notes
            listNote.getNotes().add(note);

            listNoteRepository.save(listNote);
            return listNote;
        }
        return null;
    }



    public List<Note> getAllNotes(Long listId) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        return optionalListNote.map(ListNote::getNotes).orElse(Collections.emptyList());
    }

    public Optional<Note> findNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }

    public ListNote modifyNote(long listId, Note updatedNoteData) {
        // je récupére la liste de note
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        if(optionalListNote.isEmpty()) {
            throw new EntityNotFoundException("Liste de notes non trouvée pour l'ID : " + listId);
        }

        ListNote listNote = optionalListNote.get();

        // je récupére la note
        Optional<Note> optionalNote = noteRepository.findById(updatedNoteData.getId());
        if(optionalNote.isEmpty()) {
            throw new EntityNotFoundException("Note non trouvée avec l'ID suivante : " + updatedNoteData.getId() + ". dans la liste avec l'ID : " + listId);
        }

        Note existingNote = optionalNote.get();

        // je modifie donc la note
        Note modifiedNote = noteFactory.modifyNote(
                existingNote,
                updatedNoteData.getTitle(),
                updatedNoteData.getContent().orElse(null),
                updatedNoteData.getStatus(),
                updatedNoteData.getCreationDate()
        );

        // j'enregistre la note dans la bdd avec noteRepository qui est une JPA
        noteRepository.save(modifiedNote);

        // j'enregistre aussi la liste correctement dans la bdd quand même. même si c'est vrm pas obligatoire
        listNoteRepository.save(listNote);

        return listNote;
    }

    public boolean removeNote(Long listId, Long noteId) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        if (optionalListNote.isPresent()) {
            ListNote listNote = optionalListNote.get();
            boolean removed = listNote.removeNoteById(noteId);
            if (removed) {
                listNoteRepository.save(listNote);
                return true;
            }
        }
        return false;
    }
}












