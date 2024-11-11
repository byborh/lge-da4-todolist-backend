package com.todolist.backend.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.todolist.backend.listnote.ListNote;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ListNote> listNotes = new ArrayList<>();

    public <E> User(long l, String testUser, ArrayList<E> es) {
    }

    public List<ListNote> getListNotes() {
        return listNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setListNotes(List<ListNote> listNotes) {
        this.listNotes = listNotes;
    }
}
