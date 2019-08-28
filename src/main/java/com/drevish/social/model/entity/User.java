package com.drevish.social.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
  @NotNull
  @Pattern(regexp = "[a-z,,A-Z,0-9,_]*", message = "Only latin letters, numbers and _ are allowed")
  @Length(min = 3, max = 20, message = "Password should have between 3 and 20 symbols")
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;
}
