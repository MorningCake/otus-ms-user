package ru.morningcake.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import ru.morningcake.data.entity.Post;
import ru.morningcake.model.user.PostResponse;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostMapper {

  @Mapping(target = "userId", source = "user.id")
  PostResponse toDto(Post post);

  List<PostResponse> toDtos(List<Post> posts);

}
