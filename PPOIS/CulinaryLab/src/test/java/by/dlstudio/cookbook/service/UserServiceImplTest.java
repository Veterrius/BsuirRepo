package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.dao.CookbookDAO;
import by.dlstudio.cookbook.dao.UserDAO;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.impl.UserServiceImpl;
import by.dlstudio.cookbook.util.HibernateTestUtil;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest extends HibernateTestUtil {

    private static UserDAO userDAO;
    private static CookbookDAO cookbookDAO;
    private static UserServiceImpl userService;

    @BeforeAll
    public static void setUpDb() {
        userDAO = new UserDAO();
        cookbookDAO = new CookbookDAO();
        userService = new UserServiceImpl(userDAO, cookbookDAO);
    }

    @Test
    public void getUserByNameTest_Success(){
        User validUser = new User();
        validUser.setUsername("test");
        validUser = userDAO.create(validUser);
        User actualUser = userService.getUserByName("test").orElseThrow();
        assertEquals(validUser, actualUser);
    }

    @Test
    public void createUserTest_Success() {
        User validUser = new User();
        validUser.setUsername("test2");
        validUser = userService.createUser(validUser);
        assertNotNull(validUser.getId());
        assertEquals(validUser.getCookbook(), cookbookDAO.findCookbookByUser(validUser).orElseThrow());
    }

    @Test
    public void deleteUserTest_Success() {
        User user = new User();
        user.setUsername("lel");
        user = userService.createUser(user);
        userService.deleteUser(user.getId());
        assertFalse(userService.getUserByName(user.getUsername()).isPresent());
    }

    @Test
    public void changeUsernameTest_Success() {
        User testUser = new User();
        testUser.setUsername("test");
        testUser = userService.createUser(testUser);
        testUser = userService.changeUsername(testUser.getId(), "changed");
        assertEquals(testUser, userService.getUserByName("changed").orElseThrow());
    }

    @Test
    public void getAllUsersTest_Success() {
        User addUser = new User();
        addUser.setUsername("+1");
        userService.createUser(addUser);
        assertNotEquals(0, userService.getAllUsers().size());
    }
}
