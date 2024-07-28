package ru.morningcake.utils;

import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class UserConsts {

  static UUID SELF_USER_ID = UUID.fromString("ac172001-9327-19ff-8193-2770af5f0000");
  static Set<UUID> SELF_FRIENDS_IDS = Set.of(
      UUID.fromString("")
  );
}
