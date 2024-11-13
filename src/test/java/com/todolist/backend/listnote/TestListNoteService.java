package geiffel.da4.issuetracker.user;

import geiffel.da4.issuetracker.exceptions.ResourceAlreadyExistsException;
import geiffel.da4.issuetracker.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserNoteService {

    private UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>(){{
           add(new User(1L, "Machin", Fonction.USER));
           add(new User(2L, "Chose", Fonction.USER));
           add(new User(3L, "Truc", Fonction.DEVELOPPER));
        }};
        userService = new UserLocalService(users);
    }

    @Test
    void whenGettingAll_shouldReturn3() {
        assertEquals(3, userService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        assertAll(
                () -> assertEquals(users.get(0), userService.getById(1L)),
                () -> assertEquals(users.get(2), userService.getById(3L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> userService.getById(12L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> userService.getById(4L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        User toCreate = new User(5L, "Trucmuche", Fonction.DEVELOPPER);

        assertEquals(toCreate, userService.create(toCreate));
    }

    @Test
    void whenCreatingWithSameId_shouldReturnEmpty() {
        User same_user = users.get(0);

        assertThrows(ResourceAlreadyExistsException.class, ()->userService.create(same_user));
    }

    @Test
    void whenUpdating_shouldModifyUser() {
        User initial_user = users.get(2);
        User new_user = new User(initial_user.getId(), "Updaté", initial_user.getFonction());

        userService.update(new_user.getId(), new_user);
        User updated_user = userService.getById(initial_user.getId());
        assertEquals(new_user, updated_user);
        assertTrue(userService.getAll().contains(new_user));
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        User user = users.get(2);

       assertThrows(ResourceNotFoundException.class, ()->userService.update(75L, user));
    }

    @Test
    void whenDeletingExistingUser_shouldNotBeInUsersAnymore() {
        User user = users.get(1);
        Long id = user.getId();

        userService.delete(id);
        assertFalse(userService.getAll().contains(user));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        Long id = 68L;

        assertThrows(ResourceNotFoundException.class, ()->userService.delete(id));
    }

}
