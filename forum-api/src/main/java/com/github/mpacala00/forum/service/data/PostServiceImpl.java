package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    CommentRepository commentRepository;

    @Override
    public Set<Post> getAllPosts() {
        return new HashSet<>(postRepository.findAll());
    }

    @Override
    public Post savePost(Post post) {
        if(post.getPostDate() == null) {
            post.setPostDate(LocalDateTime.now());
        }
        return postRepository.save(post);
    }

//    public List<Post> findByUser(String user) {
//        return postRepository.findByCreator(user);
//    }

    public Post findById(Long id) {
        //todo check if not null
        return postRepository.findById(id).get();
    }

}
