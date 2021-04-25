package com.github.mpacala00.forum.bootstrap;

import com.github.mpacala00.forum.security.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("admin", "admin", "admin@email.com");
        user.enableAccount(); //to skip email activation process
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);

        Post post1 = new Post();
        post1.setTitle("Hello, world!");
        post1.setBody("First post");
        post1.setCreator("Test author");
        postRepository.save(post1);
    }
}
