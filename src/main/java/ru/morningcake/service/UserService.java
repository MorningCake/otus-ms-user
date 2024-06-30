package ru.morningcake.service;


import ru.morningcake.data.entity.User;

import java.util.UUID;

/**
 * Сервис для работы с Пользовательскими аккаунтами
 */
public interface UserService {

  /**
   * Поиск пользователя по ID
   */
  User getUserById(UUID id);

}
