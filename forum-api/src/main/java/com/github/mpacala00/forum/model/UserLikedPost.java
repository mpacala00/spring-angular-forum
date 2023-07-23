package com.github.mpacala00.forum.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_liked_posts")
public class UserLikedPost {

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
    @JoinColumn(name="post_id")
    Post post;

    @NotNull
    Boolean isLike;

    public UserLikedPost(User user, Post post, Boolean isLike) {
        this.user = user;
        this.post = post;
        this.isLike = isLike;
    }
}
