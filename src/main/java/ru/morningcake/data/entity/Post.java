package ru.morningcake.data.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import ru.morningcake.entity.BaseEntity;

import javax.persistence.*;
import java.time.OffsetDateTime;


/**
 * Пользователи - данные, роли, креды, аутентификация
 */
@Entity
@Table(name = "post")
@DynamicUpdate
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

  @Column(name = "text", nullable = false)
  private String text;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "date", nullable = false)
  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now();

}
