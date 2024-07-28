package ru.morningcake.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.morningcake.benchmark.BenchmarkHelper;
import ru.morningcake.controller.user.UserApi;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.jwt.JwtToken;
import ru.morningcake.model.user.UserResponse;
import ru.morningcake.model.user.UserSearchResponse;
import ru.morningcake.security.roles.HasUserRole;
import ru.morningcake.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Обрабатывает запросы на получение и изменение данных пользователя
 */
@RestController
@RequestMapping(path = "api/ms-user")
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
  @HasUserRole
  @Override
  public ResponseEntity<UserResponse> getCurrentUser() {
    JwtToken token = userService.getCurrent();
    UserResponse response = userMapper.responseFromToken(token);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /user/{id} Получение пользователя по ID
   */
  @HasUserRole
  @Override
  public ResponseEntity<UserResponse> getUserById(UUID id) {
    User user = userService.getUserById(id);
    UserResponse response = userMapper.responseFromUser(user);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /user/search?first_name="Ива"&last_name="Ивано" - Поиск пользователей по имени и фамилии
   */
  @HasUserRole
  @Override
  public ResponseEntity<UserSearchResponse> searchUserByNames(
      @NotNull @Valid String firstName, @NotNull @Valid String lastName
  ) {
    final BenchmarkHelper benchmarkHelper = new BenchmarkHelper();
    benchmarkHelper.saveControlPoint("SELECT");
    List<User> searched = userService.searchUsersByNames(firstName, lastName);
    benchmarkHelper.saveControlPoint("MAPPING_START");
    List<UserResponse> responses = userMapper.responsesFromUsers(searched);
    benchmarkHelper.saveControlPoint("MAPPED");
    log.info(
        "SELECT - {}, MAPPING - {}",
        benchmarkHelper.getTimeDiffInSec("SELECT", "MAPPING_START"),
        benchmarkHelper.getTimeDiffInSec("MAPPING_START", "MAPPED")
    );
    return ResponseEntity.ok(new UserSearchResponse().users(responses).totalQnt((long) responses.size()));
  }
}
