package ru.morningcake.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import ru.morningcake.data.entity.User;
import ru.morningcake.jwt.JwtToken;
import ru.morningcake.model.user.UserRegistrationRequest;
import ru.morningcake.model.user.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  UserResponse responseFromToken(JwtToken token);
  UserResponse responseFromUser(User user);
  List<UserResponse> responsesFromUsers(List<User> users);

  User fromDto(UserRegistrationRequest registrationRequest);

  JwtToken toJwt(User user);

}
