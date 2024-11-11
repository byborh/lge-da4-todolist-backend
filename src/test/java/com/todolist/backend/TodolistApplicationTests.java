package com.todolist.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TodolistApplicationTests {


	@Test
	void contextLoads() {
		// Cela vérifie simplement que le contexte de l'application se charge correctement
	}

	// Ajoutez ici les tests que vous avez écrits pour UserController et ListNoteController
	@Test
	void TestUserController() throws Exception {
		// Instanciation de TestUserController ou exécution des tests ici

	}

	@Test
	void TestListNoteController() throws Exception {
		// Instanciation de TestListNoteController ou exécution des tests ici

	}

}
