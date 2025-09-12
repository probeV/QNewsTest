package com.qraft.news.domain.news.processor;

import com.qraft.news.domain.news.service.NewsService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 멀티스레드 뉴스 프로세서
 *
 * SQS 리스너의 멀티스레드 동작 시뮬레이션
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewsProcessor {

    private final NewsService newsService;
    private final NewsProcessorService newsProcessorService;

    private boolean running = true;

    // consumer 실행
    @EventListener(ApplicationReadyEvent.class)
    public void startSingleProcessor() {
        Thread consumerThread = new Thread(this::startProcessor, "ConsumerThread");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    @PreDestroy
    public void stopProcessor() {
        running = false;
    }

    public void startProcessor() {
        log.info("뉴스 프로세서 시작 :: SQS 대용 시뮬레이션");

        while (running) {
            try {
                String newsId = newsService.getNewsId();
                if (newsId != null) {
                    newsProcessorService.processNews(newsId);   // 스레드
                }
                Thread.sleep(100);                        // 폴링 간격 유지
            } catch (InterruptedException e) {
                break;
            }
        }

        log.info("뉴스 프로세서 종료");
    }
}
