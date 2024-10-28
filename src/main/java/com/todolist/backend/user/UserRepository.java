package com.todolist.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // à comprendre correctement ce que veut dire
    Optional<User> findByUserName(String username);
}
