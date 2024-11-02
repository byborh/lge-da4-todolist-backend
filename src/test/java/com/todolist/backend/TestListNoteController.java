package com.todolist.backend;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.listnote.ListNoteController;
import com.todolist.backend.listnote.ListNoteService;
import com.todolist.backend.note.NoteFactory;
import com.todolist.backend.note.TextNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(TestListNoteController.class)
public class TestListNoteController {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private NoteFactory noteFactory;
    @InjectMocks
    private ListNoteController listNoteController;

    @Mock
    private ListNoteService listNoteService;

    private ListNote testListNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testListNote = new ListNote(noteFactory); // Assure-toi que ListNote est correctement initialisé
        // Initialisation de testListNote si nécessaire
    }

    @Test
    void getAllNotes_ListExists_ReturnsNotes() throws Exception {
        when(listNoteService.getAllNotes(1L)).thenReturn(new ArrayList<>()); // Remplacer par une liste de notes si besoin

        mockMvc.perform(get("/api/listnotes/1/notes"))
                .andExpect(status().isOk());
    }

    @Test
    void findNoteById_NoteExists_ReturnsNote() throws Exception {
        // Créer une instance de TextNote avec les paramètres requis
        Long noteId = 1L;
        String title = "Test Title"; // Le titre de la note
        LocalDateTime creationDate = LocalDateTime.now(); // Date de création

        // Créer la note avec les paramètres requis
        TextNote textNote = new TextNote(title, creationDate);

        // Configurer le comportement du service
        when(listNoteService.findNoteById(noteId)).thenReturn(Optional.of(textNote));

        // Effectuer la requête et vérifier le statut de la réponse
        mockMvc.perform(get("/api/listnotes/note/{noteId}", noteId)) // Utiliser le paramètre dans l'URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title)); // Optionnel : Vérifier le titre de la note
    }


    @Test
    void addNote_ListExists_AddsNote() throws Exception {
        when(listNoteService.addNote(anyLong(), anyString(), anyString(), any(), any())).thenReturn(testListNote);

        mockMvc.perform(post("/api/listnotes/1/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Note\"}")) // Assure-toi que la note est correctement initialisée
                .andExpect(status().isOk());
    }

    @Test
    void removeNote_ListExists_RemovesNote() throws Exception {
        when(listNoteService.removeNote(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/api/listnotes/1/note/1"))
                .andExpect(status().isNoContent());
    }
}