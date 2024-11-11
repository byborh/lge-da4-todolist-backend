package com.todolist.backend;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.listnote.ListNoteService;
import com.todolist.backend.note.LongTextNote;
import com.todolist.backend.note.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebMvcTest(TestListNoteController.class)
public class TestListNoteController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListNoteService listNoteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Note note;
    private ListNote listNote;

    @BeforeEach
    void setUp() {
        note = new LongTextNote("Note Title", "Note Content", false, LocalDateTime.now());
        listNote = new ListNote(); // Adapter la création selon la structure de ListNote
    }

    // Test pour ajouter une note dans une liste spécifique
    @Test
    void addNote_ShouldReturnUpdatedListNote() throws Exception {
        Long userId = 1L;
        Long listId = 1L;

        when(listNoteService.addNote(eq(listId), eq(userId), anyString(), anyString(), any(), any()))
                .thenReturn(listNote);

        ResultActions result = mockMvc.perform(post("/api/listnote/{userId}/{listId}", userId, listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));
    }

    // Test pour récupérer toutes les notes d'une liste spécifique
    @Test
    void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        List<Note> notes = List.of(note);

        when(listNoteService.getAllNotes(eq(userId), eq(listId))).thenReturn(notes);

        ResultActions result = mockMvc.perform(get("/api/listnote/{userId}/{listId}", userId, listId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Note Title"));
    }

    // Test pour récupérer une note spécifique par ID
    @Test
    void findNoteById_ShouldReturnNote() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.findNoteById(eq(userId), eq(listId), eq(noteId))).thenReturn(Optional.of(note));

        ResultActions result = mockMvc.perform(get("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Note Title"));
    }

    // Test pour récupérer une note spécifique par ID quand elle n'existe pas
    @Test
    void findNoteById_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.findNoteById(eq(userId), eq(listId), eq(noteId))).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId));

        result.andExpect(status().isNotFound());
    }

    // Test pour modifier une note dans une liste spécifique
    @Test
    void modifyNote_ShouldReturnUpdatedListNote() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.modifyNote(eq(userId), eq(listId), any())).thenReturn(listNote);

        ResultActions result = mockMvc.perform(put("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));
    }

    // Test pour modifier une note dans une liste spécifique quand elle n'existe pas
    @Test
    void modifyNote_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.modifyNote(eq(userId), eq(listId), any())).thenReturn(null);

        ResultActions result = mockMvc.perform(put("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note)));

        result.andExpect(status().isNotFound());
    }

    // Test pour supprimer une note spécifique dans une liste
    @Test
    void removeNote_ShouldReturnNoContent() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.removeNote(eq(userId), eq(listId), eq(noteId))).thenReturn(true);

        ResultActions result = mockMvc.perform(delete("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId));

        result.andExpect(status().isNoContent());
    }

    // Test pour supprimer une note spécifique dans une liste quand elle n'existe pas
    @Test
    void removeNote_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        Long listId = 1L;
        Long noteId = 1L;

        when(listNoteService.removeNote(eq(userId), eq(listId), eq(noteId))).thenReturn(false);

        ResultActions result = mockMvc.perform(delete("/api/listnote/{userId}/{listId}/{noteId}", userId, listId, noteId));

        result.andExpect(status().isNotFound());
    }
}
