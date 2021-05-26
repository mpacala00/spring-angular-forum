package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    public Post save(Post post) {
        if(post.getPostDate() == null) {
            post.setPostDate(LocalDateTime.now());
        }
        return postRepository.save(post);
    }

    @Override
    public Post update(PostUpdateDTO post) {
        if(postRepository.findById(post.getId()).isPresent()) {
            Post postToUpdate = postRepository.findById(post.getId()).get();
            postToUpdate.setPostDate(LocalDateTime.now());
            postToUpdate.setTitle(post.getTitle());
            postToUpdate.setBody(post.getBody());
            return postRepository.save(postToUpdate);
        }
        throw new NullPointerException(String.format("Post of id=%d does not exist", post.getId()));
    }

//    public List<Post> findByUser(String user) {
//        return postRepository.findByCreator(user);
//    }

    public Post findById(Long id) {
        //todo check if not null
        return postRepository.findById(id).get();
    }

}
