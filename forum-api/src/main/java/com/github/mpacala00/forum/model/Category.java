package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @NotBlank
    @Size(max = 25)
    String name;

    @Size(max = 200)
    @NotBlank
    String description;

    //ignore comments collection when fetching posts
    @JsonIgnoreProperties("comments")
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    Set<Post> posts = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followedCategories", fetch = FetchType.EAGER)
    Set<User> followingUsers = new HashSet<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setCategory(this);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", posts=" + posts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id) &&
                name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
