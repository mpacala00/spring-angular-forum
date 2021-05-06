package com.github.mpacala00.forum.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description; //default max is 255, use constrains on front-end too

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    List<Post> posts = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setCategory(this);
    }
}
