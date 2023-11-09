package by.dlstudio.cookbook.controller;

import by.dlstudio.cookbook.entity.User;
import by.dlstudio.cookbook.service.UserService;
import by.dlstudio.cookbook.service.impl.UserServiceImpl;

import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController() {
        userService = new UserServiceImpl();
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createUser(User user) {
        return userService.createUser(user);
    }

    public User changeUsername(User principal, String newUsername) {
        return userService.changeUsername(principal.getId(), newUsername);
    }

    public void deleteUser(User principal) {
        userService.deleteUser(principal.getId());
    }

    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    public User getUserByName(String username) {
        return userService.getUserByName(username).orElseThrow();
    }
}
