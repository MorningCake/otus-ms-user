package ru.morningcake.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morningcake.controller.user.FriendApi;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.data.model.base.AffectedResponse;
import ru.morningcake.model.user.UserSearchResponse;
import ru.morningcake.security.roles.HasUserRole;
import ru.morningcake.service.FriendService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

/**
 * Обрабатывает запросы на получение и изменение данных пользователя
 */
@RestController
@RequestMapping(path = "api/ms-user")
@RequiredArgsConstructor
@Slf4j
public class FriendController implements FriendApi {

  private final FriendService friendService;
  private final UserMapper userMapper;

  /** POST /friend/{userId} : Добавление пользователя в друзья по id */
  @HasUserRole
  @Override
  public ResponseEntity<AffectedResponse> addFriendById(UUID userId) {
    boolean isAdded = friendService.addFriendById(userId);
    return ResponseEntity.ok(new AffectedResponse().affected(isAdded ? 1L : 0L));
  }

  @HasUserRole
  @Override
  public ResponseEntity<AffectedResponse> deleteFriendById(UUID userId) {
    boolean isDeleted = friendService.deleteFriendById(userId);
    return ResponseEntity.ok(new AffectedResponse().affected(isDeleted ? 1L : 0L));
  }

  @HasUserRole
  @Override
  public ResponseEntity<UserSearchResponse> getFriends(@Min(1L) @Valid Integer pageSize, @Min(1L) @Valid Integer pageNum)  {
    List<User> friendsPage = friendService.getFriends(pageSize, pageNum);
    return ResponseEntity.ok(
        new UserSearchResponse().totalQnt((long) friendsPage.size())
            .users(userMapper.responsesFromUsers(friendsPage))
    );
  }
}
