package ru.morningcake.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.morningcake.data.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query(value = "from User where id = :id")
  Optional<User> findUserById(UUID id);

  @Query(value = "from User where username = :username")
  Optional<User> findByUsername(String username);


}
