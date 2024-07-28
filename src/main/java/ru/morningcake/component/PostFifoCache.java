package ru.morningcake.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.morningcake.data.entity.Post;
import ru.morningcake.data.repository.PostRepository;
import ru.morningcake.entity.search.util.EntityUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostFifoCache extends FifoCache<UUID, UUID, Post> {

  private final PostRepository repository;

  @Value("${app.cache.posts.size:1000}")
  @Getter
  private Integer cacheSize;

  @PostConstruct
  void init() {
    super.setCacheSize(cacheSize);
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  @Async("cacheWarmingPool")
  public void warming(UUID userId) {
    log.debug("Thread {} {}: warming post cache for user {}",
        Thread.currentThread().getThreadGroup().getName(), Thread.currentThread().getName(), userId);
    List<Post> feed = repository.findFriendsFeed(userId, cacheSize, 1);
    super.warming(userId, feed, EntityUtils.getIdsListFromEntities(feed));
    log.debug("Warming post cache for user {} has been stopped!", userId);
  }

}
