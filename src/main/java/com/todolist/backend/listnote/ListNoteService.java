package com.todolist.backend.listnote;

import com.todolist.backend.note.Note;
import com.todolist.backend.note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ListNoteService {
    private final ListNoteRepository listNoteRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public ListNoteService(ListNoteRepository listNoteRepository, NoteRepository noteRepository) {
        this.listNoteRepository = listNoteRepository;
        this.noteRepository = noteRepository;
    }

    public ListNote addNote(Long listId, Note note) {
        // 1. Récupérer la ListNote par son ID
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);

        // 2. Vérifier si la note n'est pas nulle et si la ListNote existe
        if (note != null && optionalListNote.isPresent()) {
            ListNote listNote = optionalListNote.get();

            // 3. Ajouter la note à la liste
            listNote.addNote(note.getClass().getSimpleName(), note.getTitle(), note.getContent(), note.getCreationDate());

            // 4. Sauvegarder la liste mise à jour dans le repository
            listNoteRepository.save(listNote);
        }
        return null;
    }

    public boolean removeNote(Long noteId) {
        if(noteId != null) {
            Optional<ListNote> optionalListNote = listNoteRepository.findById(noteId);
            if(optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();
                listNote.removeNote(noteId);
                listNoteRepository.save(listNote);
            }
        }
        return false;
    }

    public List<Note> getAllNotes(Long listId) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        return optionalListNote.map(ListNote::getNotes).orElse(Collections.emptyList());
    }

    public Optional<Note> findNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }

}











