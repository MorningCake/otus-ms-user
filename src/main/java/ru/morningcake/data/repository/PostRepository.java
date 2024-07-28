package ru.morningcake.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.morningcake.data.entity.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

  @Query(value = "from Post where id = :id")
  Optional<Post> findPostById(UUID id);

  @Query(nativeQuery = true, value = "select * from otus_user.post " +
     "where user_id in (" +
      "select friend_id from otus_user.friends where user_id = :userId" +
      ") order by \"date\" desc limit :limit offset :offset"
  )
  List<Post> findFriendsFeed(UUID userId, int limit, int offset);

  @Query(nativeQuery = true, value = "select * from otus_user.post " +
      "where user_id = :userId " +
      "order by \"date\" desc limit :limit offset :offset"
  )
  List<Post> findUsersPosts(UUID userId, int limit, int offset);

}
