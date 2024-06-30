package ru.sberbank.femida.ms.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.morningcake.data.repository.UserRepository;
import ru.sberbank.femida.ms.user.BaseMockTest;

@Sql(scripts = {"/userTestData.sql"})
@Transactional
public class UserControllerTest extends BaseMockTest {

  private static final String USER_ID = "";

  @Autowired private UserRepository repository;

}