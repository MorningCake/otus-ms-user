package ru.morningcake.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morningcake.controller.user.PostApi;
import ru.morningcake.data.entity.Post;
import ru.morningcake.data.mapper.PostMapper;
import ru.morningcake.data.model.base.AffectedResponse;
import ru.morningcake.data.model.base.BaseResponse;
import ru.morningcake.data.model.base.UuidResponse;
import ru.morningcake.model.user.PostFeedResponse;
import ru.morningcake.model.user.PostRequest;
import ru.morningcake.model.user.PostResponse;
import ru.morningcake.security.roles.HasUserRole;
import ru.morningcake.service.PostService;

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
public class PostController implements PostApi {

  private final PostService postService;
  private final PostMapper postMapper;
  private final ObjectMapper objectMapper;

  @HasUserRole
  @Override
  public ResponseEntity<UuidResponse> createPost(@Valid PostRequest postRequest) {
    Post post = postService.createPost(postRequest.getText());
    return ResponseEntity.ok(new UuidResponse().id(post.getId()));
  }

  @HasUserRole
  @Override
  public ResponseEntity<BaseResponse> updatePostById(UUID postId, @Valid PostRequest postRequest) {
    postService.updateOwnPost(postRequest.getText(), postId);
    return ResponseEntity.ok(new BaseResponse());
  }

  @HasUserRole
  @Override
  public ResponseEntity<AffectedResponse> deletePostById(UUID postId) throws Exception {
    long affected = postService.deletePost(postId);
    return ResponseEntity.ok(new AffectedResponse().affected(affected));
  }

  @HasUserRole
  @Override
  public ResponseEntity<PostResponse> getPostById(UUID postId) throws Exception {
    Post post = postService.getPostById(postId);
    return ResponseEntity.ok(postMapper.toDto(post));
  }

  @HasUserRole
  @Override
  public ResponseEntity<PostFeedResponse> getPostsFeed(@Min(1L) @Valid Integer pageSize, @Min(1L) @Valid Integer pageNum) throws Exception {
    List<Post> postsFeedPage = postService.getPostsFeedPage(pageNum, pageSize);
    List<PostResponse> postResponses = postMapper.toDtos(postsFeedPage);
    log.info("{}", objectMapper.writeValueAsString(postResponses));
    return ResponseEntity.ok(new PostFeedResponse().posts(postResponses).total(postResponses.size()));
  }

}
