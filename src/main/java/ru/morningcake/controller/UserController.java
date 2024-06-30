package ru.morningcake.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.morningcake.controller.user.UserApi;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.data.model.base.Role;
import ru.morningcake.jwt.JwtToken;
import ru.morningcake.model.user.UserResponse;
import ru.morningcake.security.roles.HasRoles;
import ru.morningcake.service.UserService;

import java.util.Optional;
import java.util.UUID;

/**
 * Обрабатывает запросы на получение и изменение данных пользователя
 */
@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return UserApi.super.getRequest();
  }

  /**
   * GET /user/current Получение текущего пользователя
   */
  @HasRoles(roles = {Role.USER})
  @Override
  public ResponseEntity<UserResponse> getCurrentUser() {
    JwtToken token = userService.getCurrent();
    UserResponse response = userMapper.responseFromToken(token);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /user/{id} Получение пользователя по ID
   */
  @HasRoles(roles = {Role.ADMIN})
  @Override
  public ResponseEntity<UserResponse> getUserById(UUID id) {
    User user = userService.getUserById(id);
    UserResponse response = userMapper.responseFromUser(user);
    return ResponseEntity.ok(response);
  }
}
