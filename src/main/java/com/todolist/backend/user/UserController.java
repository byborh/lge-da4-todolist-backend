package com.todolist.backend.user;

import com.todolist.backend.listnote.ListNote;
import com.todolist.backend.note.NoteFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final NoteFactory noteFactory;

    @Autowired
    public UserController(UserService userService, NoteFactory noteFactory) {
        this.userService = userService;
        this.noteFactory = noteFactory;
    }

    // createUser
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Créer une nouvelle ListNote
        ListNote listNote = new ListNote(noteFactory);
        listNote.setUser(user); // Associe l'utilisateur à la ListNote
        user.getListNotes().add(listNote); // Ajoute la ListNote à l'utilisateur

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{userid}")
    public ResponseEntity<User> getUserByUserid(@PathVariable Long userid) {
        Optional<User> user = userService.findUserByUserid(userid);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // deleteUser
    @DeleteMapping("/{userId}") // Ajout de l'ID dans le chemin
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
