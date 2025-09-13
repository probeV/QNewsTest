package com.qraft.news.domain.news.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 뉴스 처리 시스템을 위한 스레드 설정
 *
 * NewsGenerator에 사용될 뉴스 자동 생성용 스레드 풀
 * NewsProcessor에 사용될 SQS 리스너 시뮬레이션용 스레드 풀
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 뉴스 생성기 전용 스레드 풀
     *
     * @return executor
     */
    @Bean("newsGeneratorExecutor")
    public Executor newsGeneratorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);     // 뉴스 생성 스레드 5개로 고정
        executor.setQueueCapacity(50);   // 스레드 개수 고정 -> 큐 사용 x
        executor.setThreadNamePrefix("newsGeneratorExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();

        log.info("뉴스 생성용 스레드 풀 초기화: Core={}, Max={}. Queue={}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }

    /**
     * 뉴스 프로세서 전용 스레드 풀 (SQS 리스너 시뮬레이션)
     *
     * 큐에서 뉴스 ID를 가져와서 브로드캐스트 처리
     * 실제 SQS의 멀티 스레드 메시지 처리 시뮬레이션
     *
     * @return executor
     */
    @Bean("newsProcessorExecutor")
    public Executor newsProcessorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("newsProcessorExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();

        log.info("뉴스 프로세서용 스레드 풀 초기화: Core={}, Max={}. Queue={}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}
