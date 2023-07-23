package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@Table(name = "posts")
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

    @Size(max = 150)
    @NotBlank(message = "Title of post must not be empty")
    String title;

    @Size(max = 10000)
    @NotBlank(message = "Post must include body")
    String body;

    LocalDateTime postDate;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="category_id")
    Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<UserLikedPost> userLikes = new HashSet<>();

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

    public Integer getLikeCount() {
        Integer userLikesCount = userLikes.stream()
                .filter(UserLikedPost::getIsLike)
                .collect(Collectors.toSet())
                .size();

        Integer userDislikesCount = userLikes.stream()
                .filter(Predicate.not(UserLikedPost::getIsLike))
                .collect(Collectors.toSet())
                .size();

        return userLikesCount - userDislikesCount;
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
