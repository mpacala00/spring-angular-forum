package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    CategoryService categoryService;
    PostRepository postRepository;
    CommentRepository commentRepository; //todo maybe remove; check other todo
    PostDTOMappingService postDTOMappingService;

    @Override
    public Post save(Post post) {
        post.setPostDate(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post savePostInCategory(Post post, Long categoryId) {
        Post savedPost = save(post);

        Category category = categoryService.findById(categoryId);
        category.addPost(post);
        categoryService.save(category);

        return savedPost;
    }

    @Override
    public Post update(PostUpdateDTO postUpdateDTO, Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Post of id=%d not found", postUpdateDTO.getId()));
        }

        Post post = postOpt.get();
        post.setPostDate(LocalDateTime.now());
        post.setTitle(postUpdateDTO.getTitle());
        post.setBody(postUpdateDTO.getBody());

        return postRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("post of id=%d does not exist", id));
        }

        return postOpt.get();
    }

    @Override
    public List<Post> findByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<PostDTO> findByCategoryIdMapToDTO(Long categoryId) {
        List<Post> posts = findByCategoryId(categoryId);

        return posts.stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        Post postToDelete = findById(postId);

        postToDelete.setCategory(null);
        postToDelete.setCreator(null);
        //todo test if removing is necessary
        postToDelete
                .getComments()
                .forEach(comment -> {
                    commentRepository.deleteById(comment.getId());
                });

        postRepository.deleteById(postId);
    }

}
