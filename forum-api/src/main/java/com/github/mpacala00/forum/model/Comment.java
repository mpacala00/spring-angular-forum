package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "comment")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    @EqualsAndHashCode.Exclude //if not excluded causes stack overflow during User fetching
    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    User creator;

    @Lob String body;
    LocalDateTime postDate;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
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
}
