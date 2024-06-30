package ru.morningcake.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.morningcake.controller.user.LoginApi;
import ru.morningcake.controller.user.RegistrationApi;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.data.model.base.UuidResponse;
import ru.morningcake.jwt.JwtToken;
import ru.morningcake.model.user.UserLoginRequest;
import ru.morningcake.model.user.UserRegistrationRequest;
import ru.morningcake.security.utils.SecurityUtils;
import ru.morningcake.service.UserService;
import ru.morningcake.utils.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Обрабатывает запросы на получение и изменение данных пользователя
 */
@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@Slf4j
public class LoginController implements LoginApi, RegistrationApi {

  private final UserService userService;
  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;
  private final HttpServletRequest httpRequest;
  private final HttpServletResponse httpResponse;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return LoginApi.super.getRequest();
  }

  @Override
  public ResponseEntity<String> login(@Valid UserLoginRequest request) {
    JwtToken jwt = userService.login(request.getUsername(), request.getPassword());
    String jwtEncoded = SecurityUtils.getJwtEncoded(jwt, objectMapper, "key");
    // костыль - для корректной работы авторизации на agw обязателен хэдер X-Jwt
    httpResponse.addHeader(Const.JWT_RESPONSE_HEADER, jwtEncoded);
    headersLogging();
    return ResponseEntity.ok(jwtEncoded);
  }

  @Override
  public ResponseEntity<UuidResponse> registration(@Valid UserRegistrationRequest request) {
    User user = userMapper.fromDto(request);
    UUID id = userService.registration(user);
    return ResponseEntity.ok(new UuidResponse().id(id).totalCount(1L));
  }

  /**
   * Логирование хэдеров, включаемое через уровни логирования в пропертях
   */
  private void headersLogging() {
    log.debug("Request Headers");
    Collections.list(httpRequest.getHeaderNames()).forEach(headerName ->
        log.debug("{}: {}", headerName, httpRequest.getHeader(headerName))
    );
    log.debug("Response Headers");
    httpResponse.getHeaderNames().forEach(headerName ->
        log.debug("{}: {}", headerName, httpResponse.getHeader(headerName))
    );
  }
}
