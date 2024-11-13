package com.todolist.backend;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.listnote.ListNoteController;
import com.todolist.backend.listnote.ListNoteService;
import com.todolist.backend.note.Note;
import com.todolist.backend.note.TextNote;
import com.todolist.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(ListNoteController.class)
public class TestListNoteController {

    @Mock
    private ListNoteService listNoteService;

    @InjectMocks
    private ListNoteController listNoteController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listNoteController).build();
    }

    // Test de l'ajout d'une note (POST /users/{userId}/{listId})
//    @Test
//    void testAddNote_success() throws Exception {
//        Note note = new TextNote("My note", "This is the content", LocalDateTime.now());
//        ListNote listNote = new ListNote();
//        listNote.setUser(new User("Bruce Wayne", "batman", "password"));
//
//        when(listNoteService.addNote(anyLong(), anyLong(), anyString(), anyString(), any(), any()))
//                .thenReturn(listNote);
//
//        mockMvc.perform(post("/users/1/1")
//                        .contentType("application/json")
//                        .content("{\"type\": \"TextNote\", \"title\": \"My note\", \"content\": \"This is the content\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.user.username").value("batman"));
//    }
//
//    // Test de récupération des notes par liste (GET /users/{userId}/{listId})
//    @Test
//    void testGetAllNotes() throws Exception {
//        Note note = new TextNote("My note", "This is the content", LocalDateTime.now());
//        List<Note> notes = List.of(note);
//
//        when(listNoteService.getAllNotes(1L, 1L)).thenReturn(Collections.singletonList(notes));
//
//        mockMvc.perform(get("/users/1/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title").value("My note"));
//    }
//
//    // Test de récupération d'une note spécifique (GET /users/{userId}/{listId}/{noteId})
//    @Test
//    void testFindNoteById_success() throws Exception {
//        Note note = new TextNote("My note", "This is the content", LocalDateTime.now());
//        when(listNoteService.findNoteById(1L, 1L, 1L)).thenReturn(Optional.of(note));
//
//        mockMvc.perform(get("/users/1/1/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("My note"));
//    }
//
//    // Test de modification d'une note (PUT /users/{userId}/{listId}/{noteId})
//    @Test
//    void testModifyNote_success() throws Exception {
//        Note note = new TextNote("My note", "Updated content", LocalDateTime.now());
//        ListNote listNote = new ListNote();
//
//        when(listNoteService.modifyNote(anyLong(), anyLong(), any())).thenReturn(listNote);
//
//        mockMvc.perform(put("/users/1/1/1")
//                        .contentType("application/json")
//                        .content("{\"type\": \"TextNote\", \"title\": \"My note\", \"content\": \"Updated content\"}"))
//                .andExpect(status().isOk());
//    }
//
//    // Test de suppression d'une note (DELETE /users/{userId}/{listId}/{noteId})
//    @Test
//    void testRemoveNote_success() throws Exception {
//        when(listNoteService.removeNote(1L, 1L, 1L)).thenReturn(true);
//
//        mockMvc.perform(delete("/users/1/1/1"))
//                .andExpect(status().isNoContent());
//    }
}
