package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="creator_id")
    User creator;

    @Size(max = 10000)
    @NotBlank(message = "Comment must not be empty")
    String body;

    LocalDateTime postDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="post_id") //cannot be nullable because of the way comments are saved
    Post post;

    @JsonIgnore
    @ManyToOne
    Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    List<Comment> childComments;

    @NotNull
    Boolean deleted;

    public Comment() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        this.postDate = now;
        this.deleted = false;
    }

    public Comment(User creator, String body) {
        super();
        this.creator = creator;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", postDate=" + postDate +
                ", creator.username=" + creator.getUsername() +
                ", childComments.size=" + childComments.size() +
                ", deleted=" + deleted +
                '}';
    }
}
