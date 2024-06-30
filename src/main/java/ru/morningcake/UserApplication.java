package ru.morningcake;

import org.springframework.boot.SpringApplication;
import ru.morningcake.annotation.OtusSpringBootApplication;

@OtusSpringBootApplication
public class UserApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }

}
