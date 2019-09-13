package com.drevish.social.model.entity;

import com.drevish.social.anno.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
  @NotNull
  @Length(min = 2, message = "Your name is too short")
  private String name;

  @Column(name = "surname")
  @NotNull
  @Length(min = 2, message = "Your surname is too short")
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
