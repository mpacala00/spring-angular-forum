package com.github.mpacala00.forum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

//todo write interface for this service

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    //encode password while saving
    public User save(User user) {
        if(user != null) {
            //encoding moved to User.class
            //user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("Successfuly saved user");

            return userRepository.save(user);
        } else {
            throw new NullPointerException("Null user cannot be saved");
        }

    }

    public Optional<User> findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            return foundUser;
        } else {
            throw new NullPointerException("User by id "+id+" cannot be found");
        }
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isPresent()) {
            return foundUser;
        } else {
            log.info("Username "+username+" not taken/ not found");
            return Optional.ofNullable(null);
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
