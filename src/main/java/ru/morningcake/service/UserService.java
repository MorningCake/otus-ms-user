package ru.morningcake.service;


import ru.morningcake.data.entity.User;
import ru.morningcake.jwt.JwtToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с Пользовательскими аккаунтами
 */
public interface UserService {

  /** Получение токена текущего пользователя */
  JwtToken getCurrent();

  /** Поиск пользователя по ID */
  User getUserById(UUID id);

  /** Поиск пользователя по username */
  Optional<User> getOptionalUserByUsername(String username);

  /** Регистрация пользователя */
  UUID registration(User user);

  /** Аутентификация пользователя */
  JwtToken login(String username, String password);
}
