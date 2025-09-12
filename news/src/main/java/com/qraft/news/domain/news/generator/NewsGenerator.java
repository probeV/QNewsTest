package com.qraft.news.domain.news.generator;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsGenerator {

    private final NewsGenerationService newsGenerationService;

    @EventListener(ApplicationReadyEvent.class)
    public void startNewsGenerators() {
        log.info("뉴스 생성 시작 :: 실제 AI 번역 시스템 시뮬레이션");
        for (int i = 0; i < 5; i++) {
            newsGenerationService.generateNews();
        }
    }

    @PreDestroy
    public void stopGeneration() {
        newsGenerationService.stop();
        log.info("뉴스 생성 종료");
    }
}
