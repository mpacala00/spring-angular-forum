package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "comment")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    User creator;

    @Size(max = 10000)
    @NotBlank(message = "Comment must not be empty")
    String body;

    LocalDateTime postDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id") //cannot be nullable because of the way comments are saved
    Post post;

    public Comment() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        this.postDate = now;
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
                '}';
    }
}
