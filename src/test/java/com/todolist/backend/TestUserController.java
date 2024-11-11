package com.todolist.backend;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.backend.listnote.ListNoteService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

@WebMvcTest(UserController.class)
public class TestUserController {
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @Mock
    private UserService userService;

    @Mock
    private ListNoteService listNoteService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        // Initialiser les autres champs requis pour éviter tout NullPointerException
    }

    // Test pour créer un utilisateur
    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.findUserByUsername(eq(user.getUsername()))).thenReturn(Optional.empty());
        when(userService.saveUser(eq(user))).thenReturn(user);

        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    // Test pour essayer de créer un utilisateur qui existe déjà
    @Test
    void createUser_ShouldReturnConflictIfUserExists() throws Exception {
        when(userService.findUserByUsername(eq(user.getUsername()))).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("L'utilisateur avec le nom : john_doe, existe déjà."));
    }

    // Test pour récupérer un utilisateur par son ID
    @Test
    void getUserByUserid_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        when(userService.findUserByUserid(userId)).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(get("/api/users/{userid}", userId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    // Test pour récupérer un utilisateur par son ID quand il n'existe pas
    @Test
    void getUserByUserid_ShouldReturnNotFound() throws Exception {
        Long userId = 1L;
        when(userService.findUserByUserid(userId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/api/users/{userid}", userId));

        result.andExpect(status().isNotFound());
    }

    // Test pour récupérer un utilisateur par son nom d'utilisateur
    @Test
    void getUserByUsername_ShouldReturnUser() throws Exception {
        String username = "john_doe";
        when(userService.findUserByUsername(username)).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(get("/api/users/username/{username}", username));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    // Test pour récupérer un utilisateur par son nom d'utilisateur quand il n'existe pas
    @Test
    void getUserByUsername_ShouldReturnNotFound() throws Exception {
        String username = "john_doe";
        when(userService.findUserByUsername(username)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/api/users/username/{username}", username));

        result.andExpect(status().isNotFound());
    }

    // Test pour supprimer un utilisateur
    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResultActions result = mockMvc.perform(delete("/api/users/{userId}", userId));

        result.andExpect(status().isNoContent());
    }
}
