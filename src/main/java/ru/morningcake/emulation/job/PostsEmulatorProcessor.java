package ru.morningcake.emulation.job;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.morningcake.data.entity.Post;
import ru.morningcake.data.entity.User;
import ru.morningcake.service.FriendService;
import ru.morningcake.service.PostService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsEmulatorProcessor {
  private final FriendService friendService;
  private final PostService postService;

  @Value("${app.emulation.posts.filePath}")
  private String postsFilePath;

  @Value("${app.emulation.posts.annId}")
  private UUID annId;

  @Value("${app.emulation.posts.qnt:10}")
  @Setter
  private Integer postsQnt;

  /** Добавить по одному посту N (qnt) случайным юзерам */
  @SneakyThrows
  @Transactional
  public List<UUID> postsPublishingEmulation() {
    Random random = new Random();
    List<User> friendsForUser = friendService.getFriendsForUser(annId, postsQnt, random.nextInt(59)+1);
    Path postsPath = Path.of(postsFilePath).toAbsolutePath().normalize();
    List<String> postTexts = Files.readAllLines(postsPath);
    int maxIndex = postTexts.size() - 1;
    List<UUID> postsIds = new ArrayList<>(postsQnt);
    for (User friend : friendsForUser) {
      int textIndex = random.nextInt(maxIndex);
      Post post = postService.createPostForUser(friend, postTexts.get(textIndex));
      postsIds.add(post.getId());
    }
    return postsIds;
  }
}
