package ru.morningcake.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.Hibernate;
//import org.hibernate.annotations.DynamicUpdate;
//import ru.morningcake.entity.BaseEntity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import java.util.Objects;


/**
 * Пользователи системы.
 */
//@Entity(name = "User")
//@Table(schema = "femida_user", name = "femida_user")
//@DynamicUpdate
@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class User
//    extends BaseEntity
{

//  /** Имя пользователя. */
//  @Column(name = "first_name", nullable = false, length = 50)
//  private String firstName;
//
//  /** Фамилия пользователя. */
//  @Column(name = "last_name", nullable = false, length = 50)
//  private String lastName;
//
//
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//    User that = (User) o;
//    return getId() != null && Objects.equals(getId(), that.getId());
//  }
//
//  @Override
//  public int hashCode() {
//    return this.getId() == null ? 43 : this.getId().hashCode();
//  }
}
