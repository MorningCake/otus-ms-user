package ru.morningcake.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.morningcake.data.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query(value = "from User where id = :id")
  Optional<User> findUserById(UUID id);

  @Query(value = "from User where username = :username")
  Optional<User> findByUsername(String username);

  @Query(nativeQuery = true, value = "select * from otus_user.otus_user " +
      "where second_name like :lastName and first_name like :firstName " +
      "order by id")
  List<User> searchByNames(String lastName, String firstName);

  @Query(nativeQuery = true, value = "select * from otus_user.otus_user " +
      "where id in (" +
      "select friend_id from otus_user.friends where user_id = :userId" +
      ") limit :limit offset :offset"
  )
  List<User> findFriends(UUID userId, int limit, int offset);

}
