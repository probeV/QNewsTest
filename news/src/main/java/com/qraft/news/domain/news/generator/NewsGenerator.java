package com.qraft.news.domain.news.generator;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsGenerator {

    private final NewsGenerationService newsGenerationService;

    private boolean running = true;

    // producer 실행
    @EventListener(ApplicationReadyEvent.class)
    public void startSingleProducer() {
        Thread producerThread = new Thread(this::startNewsGenerators, "ProducerThread");
        producerThread.setDaemon(true);
        producerThread.start();
    }

    @PreDestroy
    public void stopProducer() {
        running = false;
    }

    public void startNewsGenerators() {
        log.info("뉴스 생성 시작 :: 실제 AI 번역 시스템 시뮬레이션");

        int newsNum = 0;
        while (running) {
                try{
                    newsGenerationService.generateNews();
                    newsNum++;

                    if(newsNum == 50) {
                        newsNum = 0;
                        Thread.sleep(30000);
                    }
                } catch (InterruptedException e){
                    break;
                }
        }

        log.info("뉴스 생성 종료");
    }
}
