package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    User creator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String body;

    LocalDateTime postDate;

    //array list works fine, hashset creates infinite loop
    @ToString.Exclude
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
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
}
