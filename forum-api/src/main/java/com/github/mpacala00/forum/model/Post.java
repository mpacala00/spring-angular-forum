package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="creator_id")
    User creator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Size(max = 50)
    @NotBlank(message = "Title of post must not be empty")
    String title;

    @Size(max = 10000)
    @NotBlank(message = "Post must include body")
    String body;

    LocalDateTime postDate;

    @OneToMany(mappedBy = "post", orphanRemoval = true,fetch = FetchType.EAGER)
    Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="category_id")
    Category category;

    public Post() {
        this.postDate = LocalDateTime.now();
    }

    public Post(String title, String body) {
        super();
        this.title = title;
        this.body = body;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", postDate=" + postDate +
                '}';
    }
}
