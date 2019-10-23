package com.drevish.social.model.entity;

import com.drevish.social.anno.Email;
import com.drevish.social.anno.Password;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    @Password
    private String password;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> friends;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> upcomingFriendRequests;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> incomingFriendRequests;

    public User(Long id, @Email String email, @Password String password, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
