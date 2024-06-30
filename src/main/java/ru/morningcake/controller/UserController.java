package ru.morningcake.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.morningcake.controller.user.LoginApi;
import ru.morningcake.controller.user.RegistrationApi;
import ru.morningcake.data.mapper.UserMapper;
import ru.morningcake.data.model.base.UuidResponse;
import ru.morningcake.model.user.UserLoginRequest;
import ru.morningcake.model.user.UserRegistrationRequest;
import ru.morningcake.service.UserService;
import ru.morningcake.utils.Const;
import ru.morningcake.utils.TimeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
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
public class UserController implements LoginApi, RegistrationApi {

  private final UserService userService;
  private final UserMapper userMapper;
  private final HttpServletRequest request;
  private final ObjectMapper objectMapper;

  private final HttpServletResponse httpResponse;


  @Override
  public Optional<NativeWebRequest> getRequest() {
    return LoginApi.super.getRequest();
  }

  /**
   * GET /user/current Получение текущего пользователя - для фронта (без аудирования логина)
   *
   * @return '200' Пользователь получен, '500' Ошибка сервера
   */
//  @Override
//  public ResponseEntity<UserRepresentation> getCurrentUser() {
//    headersLogging();
//    User currentUser = userService.getCurrentUser();
//    UserRepresentation dto = userMapper.fromEntityToDto(currentUser);
//    return ResponseEntity.ok(dto);
//  }

  @Override
  @SneakyThrows
  public ResponseEntity<String> login(
      @Valid UserLoginRequest userLoginRequest) {


    // TODO STUB
    log.info("Stub - Get token");
    String token = getBase64String("{\"typ\":\"JWT\",\"alg\":\"HS256\"}") + "." +
        getBase64String(objectMapper.writeValueAsString(
            JwtToken.builder()
                .id(UUID.randomUUID())
                .firstName("Name")
                .secondName("Surname")
                .birthdate(LocalDate.of(1985, 5, 10))
                .city("Учкудук")
                .biography(null)
                .exp(TimeUtils.getExpAfterHours(24L, ZoneOffset.ofHours(4)))
                .username(userLoginRequest.getUsername())
                .accessId(UUID.randomUUID())
                .build()
    )) + "." + getBase64String("{\"key\":\"blabla\"}");
    httpResponse.addHeader(Const.JWT_RESPONSE_HEADER, token);
    return ResponseEntity.ok(token);
  }

  private String getBase64String(String source) {
    return Base64.getUrlEncoder().encodeToString(source.getBytes());
  }

  @Override
  public ResponseEntity<UuidResponse> registration(@Valid UserRegistrationRequest userRegistrationRequest) throws Exception {
    return RegistrationApi.super.registration(userRegistrationRequest);
  }

  /**
   * Логирование хэдеров, включаемое через уровни логирования в пропертях (для проверки интеграции с СУДИР)
   */
  private void headersLogging() {
    log.debug("Headers");
    Collections.list(request.getHeaderNames()).forEach(headerName ->
        log.debug("{}: {}", headerName, request.getHeader(headerName))
    );
  }
}
