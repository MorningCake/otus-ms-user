package ru.morningcake.service.impl;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.data.model.base.Role;
import ru.morningcake.data.repository.UserRepository;
import ru.morningcake.exception.SecurityException;
import ru.morningcake.jwt.JwtToken;
import ru.morningcake.security.SecurityFacade;
import ru.morningcake.service.FriendService;
import ru.morningcake.service.UserService;
import ru.morningcake.utils.CrudUtils;
import ru.morningcake.utils.ExceptionUtils;
import ru.morningcake.utils.TimeUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для работы с пользователем. Содержит в том числе методы, позволяющие получить токен
 * из security context, а также регистрации и аутентификации пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConfigurationProperties(prefix = "sberbank.roles")
public class UserServiceImpl implements UserService, FriendService {

  private final UserRepository userRepository;
  private final SecurityFacade securityFacade;
  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Value("${spring.datasource.url}")
  @Getter
  @Setter
  private String datasourceUrl;

  @Override
  public JwtToken getCurrent() {
    return securityFacade.getToken();
  }

  @Override
  @Transactional(readOnly = true)
  public User getCurrentUser() {
    JwtToken token = getCurrent();
    return getUserById(token.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public User getUserById(UUID id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> ExceptionUtils.getEntityNotFoundException(User.class.getSimpleName(), id));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> getOptionalUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  @Transactional
  public UUID registration(User user) {
    log.debug("Registration, db_url is " + datasourceUrl);
    checkUniqueUsername(user.getUsername());
    // хэшировать и переписать пароль, установить роль
    user.setPassword(getEncodedPassword(user.getPassword()));
    user.setRoles(Set.of(Role.USER));
    user = userRepository.save(user);
    log.debug("User is stored!");
    return user.getId();
  }

  @Override
  @Transactional
  public JwtToken login(String username, String password) {
    User user = getUserByUsername(username);
    if (passwordEncoder.matches(password, user.getPassword())) {
      // если пароль верный, установить время жизни токена на 24 часа
      user.setAccessId(UUID.randomUUID());
      user.setExp(TimeUtils.getExpAfterHours(24, TimeUtils.getDefaultZoneOffset()));
    } else {
      throw securityException();
    }
    userRepository.save(user);
    return userMapper.toJwt(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> searchUsersByNames(String firstName, String lastName) {
    return userRepository.searchByNames( lastName + "%", firstName + "%");
  }

  private SecurityException securityException() {
    return new SecurityException("Incorrect login or password!");
  }

  private void checkUniqueUsername(String username) {
    if (getOptionalUserByUsername(username).isPresent()) {
      throw ExceptionUtils.getEntityAlreadyExist(User.class.getSimpleName(), "username", username);
    }
  }

  private User getUserByUsername(String username) {
    return getOptionalUserByUsername(username)
        .orElseThrow(this::securityException);
  }

  private String getEncodedPassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  @Transactional
  public boolean addFriendById(UUID id) {
    User currentUser = getCurrentUser();
    User friend = getUserById(id);
    friend.addFriend(currentUser);
    return currentUser.addFriend(friend);
  }

  @Override
  @Transactional
  public boolean deleteFriendById(UUID id) {
    User currentUser = getCurrentUser();
    User friend = getUserById(id);
    return currentUser.deleteFriend(friend);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getFriends(Integer pageSize, Integer pageNum) {
    return getFriendsForUser(getCurrent().getId(), pageSize, pageNum);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getFriendsForUser(UUID userId, Integer pageSize, Integer pageNum) {
    int offset = CrudUtils.calcOffset(pageNum, pageSize);
    return userRepository.findFriends(userId, pageSize, offset);
  }

}
