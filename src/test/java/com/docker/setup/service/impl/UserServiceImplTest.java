package com.docker.setup.service.impl;

import com.docker.setup.exception.ResourceNotFoundException;
import com.docker.setup.model.User;
import com.docker.setup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
 * Unit tests for UserServiceImpl.
 *
@ExtendWith(MockitoExtension.class)
/**
 * Unit tests for {@link UserServiceImpl}.
 * that the service delegates correctly and handles not-found scenarios.
 */
@ExtendWith(MockitoExtension.class)

    // Mock the JPA repository so tests run without database
    @Mock
    private UserRepository userRepository;

    // Inject the mock into the service under test
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Save a normal user and ensure repository.save is called and returned value is propagated.
     */
    @Test
    void testSaveUser() {
        User input = new User(null, "Alice", "alice@example.com");
        User saved = new User(1L, "Alice", "alice@example.com");

        when(userRepository.save(input)).thenReturn(saved);

        User result = userService.saveUser(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
        verify(userRepository, times(1)).save(input);
    }

    /**
     * Save with null input: repository.save may accept null; ensure service returns whatever repository returns.
     */
    @Test
    void testSaveUser_NullInput() {
        // Use matcher to avoid static-analysis warnings about passing raw null
        when(userRepository.save(null)).thenReturn(null);

        assertNull(result);
        verify(userRepository).save((User) isNull());
    }

    /**
     * When a user exists, getUserById should return the user.
        verify(userRepository).save(null);
    @Test
    void testGetUserById_Found() {
        User user = new User(2L, "Bob", "bob@example.com");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(2L);

        assertSame(user, result);
        verify(userRepository).findById(2L);
    }

    /**
     * When a user is not found, service should throw ResourceNotFoundException with the expected message format.
     */
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(99L));

        // ResourceNotFoundException created with (resource, field, value)
        assertEquals("User not found with id : '99'", ex.getMessage());
        verify(userRepository).findById(99L);
    }

    /**
     * getAllUsers when repository returns data.
     */
    @Test
    void testGetAllUsers() {
        List<User> users = List.of(
                new User(1L, "A", "a@example.com"),
                new User(2L, "B", "b@example.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    /**
     * getAllUsers when repository returns empty list.
     */
    @Test
    void testGetAllUsers_Empty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    /**
     * Successful update: repository findById returns existing user and save persists modified entity.
     */
    @Test
    void testUpdateUser_Success() {
        User existing = new User(5L, "Old", "old@example.com");
        User details = new User(null, "New", "new@example.com");

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(5L, details);

        assertEquals(5L, updated.getId());
        assertEquals("New", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        verify(userRepository).findById(5L);
        verify(userRepository).save(existing);
    }

    /**
     * Update when user is not found should throw ResourceNotFoundException.
     */
    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(77L, new User()));
        verify(userRepository).findById(77L);
        verify(userRepository, never()).save(any());
    }

    /**
     * Update with null fields in userDetails: service will set fields to null (current behavior) — ensure that happens.
     */
    @Test
    void testUpdateUser_NullDetails() {
        User existing = new User(8L, "Keep", "keep@example.com");
        User details = new User(null, null, null);

        when(userRepository.findById(8L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(8L, details);

        assertEquals(8L, updated.getId());
        assertNull(updated.getName());
        assertNull(updated.getEmail());
        verify(userRepository).findById(8L);
        verify(userRepository).save(existing);
    }

    /**
     * Delete success path.
     */
    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(3L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(3L));

        verify(userRepository).existsById(3L);
        verify(userRepository).deleteById(3L);
    }

    /**
     * Delete when the id does not exist should throw ResourceNotFoundException.
     */
    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(13L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(13L));

        verify(userRepository).existsById(13L);
        verify(userRepository, never()).deleteById(any());
    }
}

