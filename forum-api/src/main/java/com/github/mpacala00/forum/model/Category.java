package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.hash.HashCode;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
@EqualsAndHashCode(exclude = {"followingUsers"})
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description; //default max is 255, use constrains on front-end too

    //ignore comments collection when fetching posts
    @JsonIgnoreProperties("comments")
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    Set<Post> posts = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followedCategories", fetch = FetchType.EAGER)
    Set<User> followingUsers = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setCategory(this);
    }
}
