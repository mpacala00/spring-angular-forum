package com.github.mpacala00.forum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_liked_comments")
public class UserLikedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name="comment_id")
    Comment comment;

    @NotNull
    Boolean isLike;

    public UserLikedComment(User user, Comment comment, Boolean isLike) {
        this.user = user;
        this.comment = comment;
        this.isLike = isLike;
    }
}
