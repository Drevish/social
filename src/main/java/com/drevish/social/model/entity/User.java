package com.drevish.social.model.entity;

import com.drevish.social.anno.Name;
import com.drevish.social.anno.Password;
import com.drevish.social.anno.Surname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Name
    private String name;

    @Column(name = "surname")
    @Surname
    private String surname;

    @Column(name = "email")
    @NotNull
    @NotBlank(message = "Can\'t be empty")
    @Email(message = "Not a valid email")
    private String email;

    @Column(name = "password")
    @Password
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
