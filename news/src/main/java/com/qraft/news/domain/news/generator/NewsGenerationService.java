package com.qraft.news.domain.news.generator;

import com.qraft.news.domain.news.dto.request.NewsRequestDto;
import com.qraft.news.domain.news.service.NewsService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsGenerationService {

    private final NewsService newsService;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Random random = new Random();

    public void stop() {
        running.set(false);
    }

    @Async("newsGeneratorExecutor")
    public void generateNews() {
        String threadName = Thread.currentThread().getName();

        while (running.get()) {
            try {
                int waitTime = 10000 + random.nextInt(4000); // 1~5초 사이
                Thread.sleep(waitTime);

                // 뉴스 생성 및 큐 추가
                NewsRequestDto newsRequestDto = createNews();
                newsService.addNews(newsRequestDto);

                log.info("[{}] 뉴스 생성 완료: {} (대기시간: {}ms)",
                        threadName, newsRequestDto.getId(), waitTime);

            } catch (InterruptedException e) {
                log.info("[{}] 뉴스 생성 스레드 인터럽트", threadName);
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("{} : 뉴스 생성 스레드 종료",  threadName);
    }

    public NewsRequestDto createNews() {
        String threadName = Thread.currentThread().getName();
        LocalDateTime now = LocalDateTime.now();

        return new NewsRequestDto(
                String.format("NEWS-%s-%d-%d-%d-%d-%d", threadName, now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond(), now.getNano()),
                String.format("뉴스제목-%s", threadName),
                String.format("본문-%s", threadName),
                LocalDateTime.now()
        );
    }
}