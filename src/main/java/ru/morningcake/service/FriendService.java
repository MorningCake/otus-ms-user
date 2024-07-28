package ru.morningcake.service;

import ru.morningcake.data.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с Друзьями
 */
public interface FriendService {

  /** Добавление пользователя в друзья текущего пользователя */
  boolean addFriendById(UUID id);

  /** Удаление пользователя из друзей текущего пользователя */
  boolean deleteFriendById(UUID id);

  /** Получить страницу друзей для текущего пользователя */
  List<User> getFriends(Integer pageSize, Integer pageNum);

  /** Получить страницу всех друзей для пользователя */
  List<User> getFriendsForUser(UUID userId, Integer pageSize, Integer pageNum);
}
