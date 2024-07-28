package ru.morningcake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@EnableAsync
public class SchedulerConfig {

  @Bean
  public Executor postsEmulationPool() {
    return Executors.newSingleThreadScheduledExecutor();
  }

  @Bean
  public Executor cacheWarmingPool() {
    return Executors.newSingleThreadExecutor();
  }


}
