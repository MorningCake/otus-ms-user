package ru.morningcake.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.morningcake.component.PostFifoCache;
import ru.morningcake.data.entity.Post;
import ru.morningcake.data.entity.User;
import ru.morningcake.data.repository.PostRepository;
import ru.morningcake.entity.search.util.EntityUtils;
import ru.morningcake.exception.data.BadDataException;
import ru.morningcake.service.PostService;
import ru.morningcake.service.UserService;
import ru.morningcake.utils.CrudUtils;
import ru.morningcake.utils.ExceptionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserService userService;
  private final PostFifoCache postCache;

  @Transactional(readOnly = true)
  @Override
  public Optional<Post> getOptionalPostById(UUID id) {
    return postRepository.findPostById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public Post getPostById(UUID id) {
    return getOptionalPostById(id).orElseThrow(
        () -> ExceptionUtils.getEntityNotFoundException(Post.class.getSimpleName(), id));
  }

  @Transactional
  @Override
  public Post createPost(String text) {
    return createPostForUser(userService.getCurrentUser(), text);
  }

  @Transactional
  @Override
  public Post createPostForUser(User user, String text) {
    Post post = Post.builder().text(text).user(user).build();
    post = postRepository.save(post);
    postCache.addCacheForAll(EntityUtils.getIdsSetFromEntities(user.getFriends()), post.getId(), post);
    return post;
  }

  @Transactional
  @Override
  public void updateOwnPost(String text, UUID postId) {
    User currentUser = userService.getCurrentUser();
    Post post = getPostById(postId);
    checkPostAuthor(currentUser, post);
    post.setText(text);
    postCache.putCacheForAll(EntityUtils.getIdsSetFromEntities(currentUser.getFriends()), post.getId(), post);
    postRepository.save(post);
  }

  @Transactional
  @Override
  public long deletePost(UUID id) {
    User currentUser = userService.getCurrentUser();
    Optional<Post> optionalPost = getOptionalPostById(id);
    if (optionalPost.isEmpty()) return 0L;
    Post deleted = optionalPost.get();
    checkPostAuthor(currentUser, deleted);
    postRepository.delete(deleted);
    postCache.evictCacheForAll(EntityUtils.getIdsSetFromEntities(currentUser.getFriends()), deleted.getId());
    return 1L;
  }

  @Override
  @Transactional(readOnly = true)
  @SneakyThrows
  public List<Post> getPostsFeedPage(int pageNum, int potentialPageSize) {
    int pageSize = potentialPageSize < postCache.getCacheSize() ? potentialPageSize : postCache.getCacheSize();
    User currentUser = userService.getCurrentUser();
    List<Post> posts;
    try {
      posts = postCache.getPosts(currentUser.getId(), pageNum, pageSize);
    } catch (BadDataException ex) {
      return List.of();
    }
    if (posts.isEmpty()) {
      int offset = CrudUtils.calcOffset(pageNum, pageSize);
      postCache.warming(currentUser.getId());
      return postRepository.findFriendsFeed(currentUser.getId(), pageSize, offset);
    } else {
      return posts;
    }
  }

  @Transactional(readOnly = true)
  @Override
  public List<Post> getPostsForUser(User user, int pageNum, int pageSize) {
    int offset = CrudUtils.calcOffset(pageNum, pageSize);
    return postRepository.findUsersPosts(user.getId(), pageSize, offset);
  }

  private void checkPostAuthor(User currentUser, Post post) {
    if (!post.getUser().equals(currentUser)) {
      throw new SecurityException("User " + currentUser.getId() + " isn't the author of the post " + post.getId());
    }
  }
}
