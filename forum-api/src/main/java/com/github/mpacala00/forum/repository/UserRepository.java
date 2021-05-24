package com.github.mpacala00.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mpacala00.forum.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findByEmail(String email);
}
