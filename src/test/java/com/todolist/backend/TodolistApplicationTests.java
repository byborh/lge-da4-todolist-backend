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
		TestUserController testUserController = new TestUserController();
		testUserController.createUser_ShouldReturnCreatedUser();
		testUserController.createUser_WhenUserExists_ShouldReturnConflict();
		testUserController.getUserByUserid_ShouldReturnUser();
		testUserController.getUserByUsername_ShouldReturnUser();
		testUserController.getUserByUserid_WhenUserNotFound_ShouldReturnNotFound();
	}

	@Test
	void TestListNoteController() throws Exception {
		// Instanciation de TestListNoteController ou exécution des tests ici
		TestListNoteController testListNoteController = new TestListNoteController();
		testListNoteController.addNote_ShouldReturnUpdatedListNote();
		testListNoteController.getAllNotes_ShouldReturnListOfNotes();
		testListNoteController.modifyNote_ShouldReturnUpdatedListNote();
		testListNoteController.findNoteById_ShouldReturnNote();
		testListNoteController.findNoteById_ShouldReturnNotFound();
		testListNoteController.findNoteById_ShouldReturnNotFound();
		testListNoteController.removeNote_ShouldReturnNoContent();
	}

}
