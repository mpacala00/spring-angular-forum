package com.github.mpacala00.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.mpacala00.forum.security.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "`user`") //user is reserved keyword in postgres, hence the weird quotation marks
@Getter
@Setter
@EqualsAndHashCode(exclude = { "followedCategories", "comments", "posts" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @NotBlank
    String username;

    @JsonIgnore
    @NotBlank
    String password;

    @NotBlank
    String email;

    @NotNull
    boolean enabled;

    @NotNull
    @Enumerated(EnumType.STRING)
    Role role;

    @JsonIgnoreProperties("creator")
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Comment> comments = new HashSet<>();

    @JsonIgnoreProperties("creator")
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Post> posts = new HashSet<>();

    @JsonIgnoreProperties("followingUsers")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followed_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> followedCategories = new HashSet<>();

    public User() {
        this.enabled = false;
        this.role = Role.ROLE_USER;
    }

    public User(@NonNull String username, @NonNull String password, @NonNull String email) {
        super();
        this.username = username;
        //todo implement password encoding that works
        //this.password = passwordEncoder().encode(password);
        this.password = password;
        this.email = email;
    }

    public void followCategory(Category category) {
        getFollowedCategories().add(category);
        category.getFollowingUsers().add(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void enableAccount() { this.enabled = true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.role.getAuthorities())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
