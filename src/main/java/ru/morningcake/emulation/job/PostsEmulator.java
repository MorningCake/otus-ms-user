package ru.morningcake.emulation.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Async("postsEmulationPool")
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.emulation.posts", name = "enabled", havingValue = "true")
public class PostsEmulator {
  private final PostsEmulatorProcessor processor;

  @Scheduled(
      fixedDelayString = "${app.emulation.posts.delay}",
      initialDelayString = "${app.emulation.posts.initDelay}",
      timeUnit = TimeUnit.MINUTES
  )
  public void addPostsToUserGroup() {
    log.info("PostsEmulator start!");
    List<UUID> postsIds = processor.postsPublishingEmulation();
    log.info("PostsEmulator stop! Create posts: " + postsIds.stream().map(UUID::toString).collect(Collectors.joining(", ")));
  }
}
