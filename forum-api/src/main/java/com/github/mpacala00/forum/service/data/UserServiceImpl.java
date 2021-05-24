package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
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

    @Override
    public Set<User> findAll() {
        return new HashSet<>(userRepository.findAll());
    }

    //transactional as User entity became so big its fields are stored in several records
    @Transactional
    public User findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()) {
            return foundUser.get();
        }
        log.info("User of id="+id+" not found");
        return null;
    }

    @Transactional
    public User findByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isPresent()) {
            return foundUser.get();
        }
        log.info("Username "+username+" not found");
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void delete(User entity) {
        userRepository.delete(entity);
        log.info("User {} successfully deleted", entity.getUsername());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("User of id={} successfully deleted", id);
    }

    public List<Comment> getComments(Long userId) {
        return new ArrayList<>(userRepository.findById(userId).get().getComments());
    }

    public List<Post> getPosts(Long userId) {
        return new ArrayList<>(userRepository.findById(userId).get().getPosts());
    }

    //optional is used in authentication, so this method had to stay
    @Override
    public Optional<User> findOptionalByUsername(String username) {
        return userRepository.findByUsername(username);
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
