// src/main/java/com/docker/setup/service/impl/UserServiceImpl.java
package com.docker.setup.service.impl;

import com.docker.setup.model.User;
import com.docker.setup.repository.UserRepository;
import com.docker.setup.service.UserService;
import com.docker.setup.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- CREATE ---
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // --- READ BY ID ---
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    // --- READ ALL ---
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- UPDATE ---
    @Override
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        return userRepository.save(existingUser);
    }

    // --- DELETE ---
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}