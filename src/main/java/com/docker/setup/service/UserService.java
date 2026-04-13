// src/main/java/com/docker/setup/service/UserService.java

package com.docker.setup.service;

import com.docker.setup.model.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);

    // Returns User directly (exception thrown in Impl if not found)
    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User userDetails);

    // Throws exception if not found
    void deleteUser(Long id);
}