package com.todolist.backend.listnote;

import com.todolist.backend.note.Note;
import com.todolist.backend.note.NoteFactory;
import com.todolist.backend.note.NoteRepository;
import com.todolist.backend.user.User;
import com.todolist.backend.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final UserRepository userRepository;
    private final ListNoteFactory listNoteFactory;

    @Autowired
    public ListNoteService(ListNoteRepository listNoteRepository, NoteRepository noteRepository, NoteFactory noteFactory, UserRepository userRepository, ListNoteFactory listNoteFactory) {
        this.listNoteRepository = listNoteRepository;
        this.noteRepository = noteRepository;
        this.noteFactory = noteFactory;
        this.userRepository = userRepository;
        this.listNoteFactory = listNoteFactory;
    }


    @Transactional // on dirait que c'est pour s'assurer que les transactions fonctionnent correctement
    public ListNote addNote(Long listId, Long userId, String type, String title, Optional<String> content, LocalDateTime creationDate) {
        // Rechercher l'utilisateur par son ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Trouver la liste de notes associée à l'utilisateur avec le listId
            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            if (optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();

                // Créer la note sans besoin de la connaissance de la liste
                Note note = noteFactory.createNote(listNote, type, title, content.orElse(null), false, creationDate);
                System.out.println("Voici la liste note correspondante : " + note.getListNote());
                note.setListNote(listNote);

                if (note == null) {
                    throw new IllegalArgumentException("Le type de note est incorrect : " + type);
                }

                System.out.println("Utilisateur trouvé : " + user.getUsername());
                System.out.println("Liste de notes trouvée avec ID : " + listNote.getId());
                System.out.println("Note créée de type : " + type + " avec titre : " + title);

                // Ajouter la note à la liste de notes
                listNote.getNotes().add(note);
                listNote.addNote(note); // la méthode que j'avais créé dans listnote et que j'ai oublié d'utiliser

                noteRepository.save(note);
                // Sauvegarder la liste mise à jour
                listNoteRepository.save(listNote);
                return listNote;
            }
        }
        return null;
    }

    @Transactional
    public ListNote addListNote(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            ListNote listNote = listNoteFactory.createListNote(user);
            return listNoteRepository.save(listNote);
        }
        throw new EntityNotFoundException("Utilisateur non trouvé pour l'ID : " + userId);
    }

    public List<Note> getAllNotes(Long userId, Long listId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Trouver la liste de notes associée à l'utilisateur avec le listId
            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            return optionalListNote.map(ListNote::getNotes).orElse(Collections.emptyList());
        }
        return null;
    }

    public Optional<Note> findNoteById(Long userId, Long listId, Long noteId) {
        // Rechercher l'utilisateur par son ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Trouver la liste de notes associée à l'utilisateur avec le listId
            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            // Si la liste de notes est présente, chercher la note par son ID
            if (optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();

                // Chercher la note dans la liste de notes de la liste trouvée
                return listNote.getNotes().stream()
                        .filter(note -> note.getId().equals(noteId))
                        .findFirst();  // Retourner la note correspondante si elle est trouvée
            }
        }
        return Optional.empty();
    }


    // ça met pas à jour le type de note, c bzr
    public ListNote modifyNote(Long userId, Long listId, Note updatedNoteData) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Trouver la liste de notes associée à l'utilisateur avec le listId
            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            // Si la liste de notes est présente, chercher la note par son ID
            if (optionalListNote.isPresent()) {
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
        }
        return null;
    }

    public boolean removeNote(Long userId, Long listId, Long noteId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Trouver la liste de notes associée à l'utilisateur avec le listId
            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            // Si la liste de notes est présente, chercher la note par son ID
            if (optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();

                boolean removed = listNote.removeNoteById(noteId);
                if (removed) {
                    listNoteRepository.save(listNote);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeList2(Long userId, Long listId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            if(optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();
//                boolean removed = listNote.removeListNoteById(listId);
//                if(removed) {
//                    listNoteRepository.save(listNote);
//                    return true;
//                }
            }

        }

        return false;
    }

    @Transactional
    public boolean removeList(Long userId, Long listId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<ListNote> optionalListNote = user.getListNotes().stream()
                    .filter(listNote -> listNote.getId().equals(listId))
                    .findFirst();

            if(optionalListNote.isPresent()) {
                ListNote listNote = optionalListNote.get();
                user.getListNotes().remove(listNote);
                listNoteRepository.deleteById(listId);
                return true;
            }
        }
        return false;
    }
}