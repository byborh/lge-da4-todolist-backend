package com.todolist.backend;


import com.todolist.backend.user.User;
import com.todolist.backend.user.UserController;
import com.todolist.backend.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class TestUserController {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("ton_username");
        testUser.setPassword("ton_password");
    }

    @Test
    void getUserByUsername_UserExists_ReturnsUser() throws Exception {
        when(userService.findUserByUsername("ton_username")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/ton_username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ton_username"));
    }

    @Test
    void getUserByUsername_UserDoesNotExist_ReturnsNotFound() throws Exception {
        when(userService.findUserByUsername("nonexistent_username")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/nonexistent_username"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ValidUser_CreatesUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ton_username\",\"password\":\"ton_password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ton_username"));
    }

    @Test
    void deleteUser_UserExists_ReturnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_UserDoesNotExist_ReturnsNotFound() throws Exception {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}

