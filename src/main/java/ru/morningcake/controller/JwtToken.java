package ru.morningcake.controller;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class JwtToken {
  private UUID id;
  private String firstName;
  private String secondName;
  private LocalDate birthdate;
  private String biography;
  private String city;
  private String username;
  private UUID accessId;
  private Long exp;
}
