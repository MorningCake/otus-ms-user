package ru.morningcake.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.service.UserService;

import java.util.UUID;

/**
 * Сервис для работы с пользователем.
 * Содержит в том числе методы, позволяющие сопоставить текущего пользователя из токена (зашит в security context)
 * и на базе данных, а также регистрации пользователя в АС
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConfigurationProperties(prefix = "sberbank.roles")
public class UserServiceImpl implements UserService {

//  private final UserRepository userRepository;
  private final UserMapper userMapper;



  @Override
//  @Transactional(readOnly = true)
  public User getUserById(UUID id) {
//    log.info("Get user by id: {}", id);
//    return userRepository.findByIdOrThrowException(id);
    return null;
  }

}
