package com.todolist.backend;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.note.NoteFactory;
import com.todolist.backend.user.User;
import com.todolist.backend.user.UserController;
import com.todolist.backend.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Optional;

@WebMvcTest(TestUserController.class)
public class TestUserController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private NoteFactory noteFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private ListNote listNote;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testUser", new ArrayList<>());
        listNote = new ListNote(noteFactory);
    }

    // Test pour la création d'utilisateur
    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userService.saveUser(user)).thenReturn(user);

        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    // Test pour la création d'utilisateur avec un nom déjà existant
    @Test
    void createUser_WhenUserExists_ShouldReturnConflict() throws Exception {
        when(userService.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isConflict())
                .andExpect(content().string("L'utilisateur avec le nom : testUser, existe déjà."));
    }

    // Test pour récupérer un utilisateur par ID
    @Test
    void getUserByUserid_ShouldReturnUser() throws Exception {
        when(userService.findUserByUserid(1L)).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(get("/api/users/{userid}", 1L));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    // Test pour récupérer un utilisateur par nom d'utilisateur
    @Test
    void getUserByUsername_ShouldReturnUser() throws Exception {
        when(userService.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(get("/api/users/username/{username}", "testUser"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    // Test pour récupérer un utilisateur avec un ID inexistant
    @Test
    void getUserByUserid_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.findUserByUserid(1L)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/api/users/{userid}", 1L));

        result.andExpect(status().isNotFound());
    }

    // Test pour supprimer un utilisateur
    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        ResultActions result = mockMvc.perform(delete("/api/users/{userId}", 1L));

        result.andExpect(status().isNoContent());
    }
}
