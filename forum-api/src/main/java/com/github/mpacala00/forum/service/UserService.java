package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    //transactional as User entity became so big its fields are stored in several records
    @Transactional
    public Optional<User> findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            return foundUser;
        } else {
            log.info("User of id="+id+" not found");
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isPresent()) {
            return foundUser;
        } else {
            log.info("Username "+username+" not found");
            return Optional.empty();
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Comment> getComments(Long userId) {
        return new ArrayList<>(userRepository.findById(userId).get().getComments());
    }

    public List<Post> getPosts(Long userId) {
        return new ArrayList<>(userRepository.findById(userId).get().getPosts());
    }

    //in order for this method to work token has to be passed in Authorization header
    //tl;dr: don't use this method in /public/* mappings
    public String getUsernameFromToken() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        throw new Exception("Failed to retreive username. Token not found");
    }

}
