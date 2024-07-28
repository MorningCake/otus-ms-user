package ru.morningcake.service;

import ru.morningcake.data.entity.Post;
import ru.morningcake.data.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с Постами пользователя
 */
public interface PostService {

  Optional<Post> getOptionalPostById(UUID id);

  /** Поиск поста текущего пользователя по ID */
  Post getPostById(UUID id);

  /** Создание поста текущего пользователя */
  Post createPost(String text);

  /** Обновление поста текущего пользователя */
  void updateOwnPost(String text, UUID postId);

  /** Удалние поста текущего пользователя */
  long deletePost(UUID id);

  /**
   * Получить ленту постов друзей текущего пользователя (с пагинацией).
   * После прогрева лента формируется из кэша.
   */
  List<Post> getPostsFeedPage(int pageNum, int pageSize);

  /** Получить ленту постов по для пользователя (с пагинацией). */
  List<Post> getPostsForUser(User user, int pageNum, int pageSize);

  /** Создать пост для пользователя */
  Post createPostForUser(User user, String text);
}
