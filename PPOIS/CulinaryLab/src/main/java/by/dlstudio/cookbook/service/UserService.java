package by.dlstudio.cookbook.service;

import by.dlstudio.cookbook.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    void deleteUser(long userId);

    User changeUsername(long userId, String newUsername);

    Optional<User> getUserByName(String username);

    List<User> getAllUsers();
}
