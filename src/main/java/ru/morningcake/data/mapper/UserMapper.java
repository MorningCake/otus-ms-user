package ru.morningcake.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import ru.morningcake.data.entity.User;
import ru.morningcake.model.user.UserResponse;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  UserResponse fromEntityToDto(User entity);

}
