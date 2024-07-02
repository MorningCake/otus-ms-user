package ru.morningcake.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import ru.morningcake.data.model.base.Role;
import ru.morningcake.entity.BaseEntity;
import ru.morningcake.model.user.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


/**
 * Пользователи - данные, роли, креды, аутентификация
 */
@Entity
@Table(name = "otus_user")
@DynamicUpdate
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "second_name", nullable = false)
  private String secondName;

  @Column(name = "birthdate", nullable = false)
  private LocalDate birthdate;

  @Column(name = "sex", nullable = false)
  @Enumerated(EnumType.STRING)
  private Sex sex;

  @Column(name = "biography")
  private String biography;

  @Column(name = "city")
  private String city;

  @ElementCollection(targetClass = Role.class)
  @JoinTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "access_id")
  private UUID accessId;

  @Column(name = "exp")
  private Long exp;

}
