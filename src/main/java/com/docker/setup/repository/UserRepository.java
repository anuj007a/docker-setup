// src/main/java/com/example/demo/repository/UserRepository.java

package com.docker.setup.repository;

import com.docker.setup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic CRUD methods (save, findById, findAll, delete) are automatically included.
}