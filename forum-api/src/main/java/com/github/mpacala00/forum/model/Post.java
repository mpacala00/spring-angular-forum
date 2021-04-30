package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    //@ManyToOne //post has one creator, creator has many posts
    String creator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Lob
    String body;

    //todo use LocalDate
    Date date;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="category_id")
    Category category;

    public Post() {
        date = new Date();
    }

    public Post(String creator, String title, String body) {
        super();
        this.creator = creator;
        this.title = title;
        this.body = body;
    }
}
