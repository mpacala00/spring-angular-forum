package com.github.mpacala00.forum.controllers;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.dto.CommentDto;
import com.github.mpacala00.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.service.PostService;
import com.github.mpacala00.forum.service.UserService;

import java.util.Date;
import java.util.List;

@RestController
//@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ForumController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public ForumController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("public/posts")
    public List<Post> posts() {
        return postService.getAllPosts();
    }

    @PostMapping("/post")
    public String publishPost(@RequestBody Post post) {
//        CustomUserDetails userDetails =
//                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(post.getDate() == null) {
            Date date = new Date();
            post.setDate(date);
        }
        //post.setCreator(userService.getUser(userDetails.getUsername()));
        post.setCreator(post.getCreator());

        postService.insertPost(post);
        return "Post was published";
    }

    @PostMapping("/post/{id}/comment")
    public ResponseEntity<String> postComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
        //todo check if Long.valueOf(id) is of type Long
        Comment posted = commentService.postComment(commentDto, Long.valueOf(id));
        return new ResponseEntity<>("Comment successfully added", HttpStatus.CREATED);
    }

//    @GetMapping("posts/{username}")
//    public List<Post> postsByUsername(@PathVariable String username) {
//        return postService.findByUser(userService.getUser(username));
//    }

    //authO below

    @GetMapping("/private")
    public String privateArea() {
        return "private";
    }

}
