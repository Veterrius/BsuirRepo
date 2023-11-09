package by.dlstudio.cookbook.service.impl;

import by.dlstudio.cookbook.entity.Cookbook;
import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.dao.CookbookDAO;
import by.dlstudio.cookbook.dao.UserDAO;
import by.dlstudio.cookbook.service.UserService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final CookbookDAO cookbookDAO;

    /**
     * This constructor has a basic initialization for {@link UserDAO}
     * and {@link CookbookDAO}
     */
    public UserServiceImpl() {
        userDAO = new UserDAO();
        cookbookDAO = new CookbookDAO();
    }

    /**
     * This constructor may be used instead to replace the basic initialization
     * Currently used in testing
     * @param userDAO is an UserDAO we want to use instead of a basic one
     * @param cookbookDAO is a CookbookDAO we want to use instead of a basic one
     */
    public UserServiceImpl(UserDAO userDAO, CookbookDAO cookbookDAO) {
        this.userDAO = userDAO;
        this.cookbookDAO = cookbookDAO;
    }

    /**
     * This method creates a user in a database, also creating it's cookbook.
     * It uses {@link UserDAO} class method to do that.
     * @param user is a user we want to create
     * @return created user
     */
    @Override
    public User createUser(@NotNull User user) {
        Cookbook cookbook = new Cookbook();
        cookbook.setUser(user);
        user.setCookbook(cookbook);
        return userDAO.create(user);
    }

    /**
     * This method deletes a user by it's id using methods from {@link UserDAO}
     * Before that, it deletes a cookbook of that uses by using method from {@link CookbookDAO}
     * @param userId is an id of a user we want to delete
     */
    @Override
    public void deleteUser(long userId) {
        User userToDelete = userDAO.findOne(userId).orElseThrow();
        Cookbook userCB = userToDelete.getCookbook();
        userToDelete.setCookbook(null);
        userDAO.update(userToDelete);
        cookbookDAO.delete(userCB);
        userDAO.delete(userToDelete);
    }

    /**
     * This method changes the username of a user by it's id.
     * It firstly finds that user by that id. It uses methods from {@link UserDAO}
     * @param userId is an id of a user we want to change username of
     * @param newUsername is a new username
     * @return user with changed username
     */
    @Override
    public User changeUsername(long userId, String newUsername) {
        User userFromDb = userDAO.findOne(userId).orElseThrow();
        userFromDb.setUsername(newUsername);
        return userDAO.update(userFromDb);
    }

    /**
     * Thot method finds user by username using method from {@link UserDAO} class
     * @param username is a username by which we want to find a user
     * @return Optional of found user
     */
    @Override
    public Optional<User> getUserByName(String username) {
        return userDAO.findUserByName(username);
    }

    /**
     * This method gets all users using method from {@link UserDAO}
     * @return list of found users
     */
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}
