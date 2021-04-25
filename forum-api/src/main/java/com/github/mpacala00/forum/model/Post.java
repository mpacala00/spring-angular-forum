package com.github.mpacala00.forum.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Post {

    //@ManyToOne //post has one creator, creator has many posts
    private String creator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String body;
    private Date date;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();

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
