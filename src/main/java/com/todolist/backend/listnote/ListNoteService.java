package com.todolist.backend.listnote;

import com.todolist.backend.note.Note;
import com.todolist.backend.note.NoteFactory;
import com.todolist.backend.note.NoteRepository;
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

    public ListNote addNote(Long listId, String type, String title, Optional<String> content, LocalDateTime creationDate) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        if (optionalListNote.isPresent()) {
            ListNote listNote = optionalListNote.get();
            Note note = noteFactory.createNote(type, title, content.orElse(null), creationDate);
            listNote.getNotes().add(note);
            listNoteRepository.save(listNote);
            return listNote;
        }
        return null;
    }

    public boolean removeNote(Long listId, Long noteId) {
        Optional<ListNote> optionalListNote = listNoteRepository.findById(listId);
        if (optionalListNote.isPresent()) {
            ListNote listNote = optionalListNote.get();
            boolean removed = listNote.removeNoteById(noteId); // Ajout d'une vérification de suppression
            if (removed) {
                listNoteRepository.save(listNote);
                return true;
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












