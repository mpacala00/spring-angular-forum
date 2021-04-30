package com.github.mpacala00.forum.bootstrap;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.repository.CategoryRepository;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;
import com.github.mpacala00.forum.security.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

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
        Post savedPost = postRepository.save(post1);

        Category cat1 = new Category();
        cat1.setName("Fist category");
        Category savedCat1 = categoryRepository.save(cat1);

        savedCat1.addPost(savedPost);
        categoryRepository.save(savedCat1);
        postRepository.save(savedPost);

    }
}
