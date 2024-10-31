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
		TestUserController TestUserController = new TestUserController();
		TestUserController.getUserByUsername_UserExists_ReturnsUser();
		TestUserController.getUserByUsername_UserDoesNotExist_ReturnsNotFound();
		TestUserController.createUser_ValidUser_CreatesUser();
		TestUserController.deleteUser_UserExists_ReturnsNoContent();
		TestUserController.deleteUser_UserDoesNotExist_ReturnsNotFound();
	}

	@Test
	void TestListNoteController() throws Exception {
		// Instanciation de TestListNoteController ou exécution des tests ici
		TestListNoteController TestListNoteController = new TestListNoteController();
		TestListNoteController.getAllNotes_ListExists_ReturnsNotes();
		TestListNoteController.findNoteById_NoteExists_ReturnsNote();
		TestListNoteController.addNote_ListExists_AddsNote();
		TestListNoteController.removeNote_ListExists_RemovesNote();
	}

}
