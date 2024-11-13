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






package geiffel.da4.issuetracker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import geiffel.da4.issuetracker.exceptions.ExceptionHandlingAdvice;
import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = UserController.class)
@Import(ExceptionHandlingAdvice.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>() {{
            add(new User(1L, "Machin", "mpd123"));
            add(new User(2L, "Chose", "mpd123"));
            add(new User(3L, "Truc", "mpd123"));
            add(new User(14L, "higher", "mpd123"));
            add(new User(7L, "lower", "mpd123"));
            add(new User(28L, "way higher", "mpd123"));
        }};
        when(userService.getAll()).thenReturn(users);
        when(userService.getById(7L)).thenReturn(users.get(4));
        when(userService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void whenGettingUser_shouldGet6_andBe200() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(6))
        ).andDo(print());
    }

    @Test
    void whenGettingId7L_shouldReturnSame() throws Exception{
        mockMvc.perform(get("/api/users/7")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.id", is(7))
        ).andExpect(jsonPath("$.nom", is("lower"))
        ).andReturn();
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        mockMvc.perform(get("/api/users/49")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingNew_shouldReturnLink_andShouldBeStatusCreated() throws Exception {
        User new_user = new User(89L, "nouveau");
        ArgumentCaptor<User> user_received = ArgumentCaptor.forClass(User.class);
        when(userService.create(any())).thenReturn(new_user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new_user))
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/api/users/"+new_user.getId())
        ).andDo(print());

        verify(userService).create(user_received.capture());
        assertEquals(new_user, user_received.getValue());
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        when(userService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.users.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceiveUserToUpdate_andReturnNoContent() throws Exception {
        User initial_user = users.get(1);
        User updated_user = new User(initial_user.getId(), "updated");
        ArgumentCaptor<User> user_received = ArgumentCaptor.forClass(User.class);

        mockMvc.perform(put("/api/users/"+initial_user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_user))
        ).andExpect(status().isNoContent());

        verify(userService).update(anyLong(), user_received.capture());
        assertEquals(updated_user, user_received.getValue());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;

        mockMvc.perform(delete("/api/users/"+id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(userService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }
}