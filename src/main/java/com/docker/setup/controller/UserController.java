// src/main/java/com/docker/setup/controller/UserController.java
package com.docker.setup.controller;

import com.docker.setup.model.User;
import com.docker.setup.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. CREATE (Returns 200 OK by default, wrapped globally)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Set status to 201 Created
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // 2. READ ALL (Returns 200 OK, wrapped globally)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 3. READ By ID (Returns 200 OK or 404 from service)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 4. UPDATE (Returns 200 OK or 404 from service)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    // 5. DELETE (Returns 204 No Content or 404 from service)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Set status to 204 No Content
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}