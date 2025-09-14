package com.qraft.news.domain.news.generator;

import com.qraft.news.domain.news.dto.request.NewsRequestDto;
import com.qraft.news.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsGenerationService {

    private final NewsService newsService;
    private static final AtomicLong counter = new AtomicLong(0);

    @Async("newsGeneratorExecutor")
    public void generateNews() {
        try {
            String threadName = Thread.currentThread().getName();

            // 뉴스 생성 및 큐 추가
            NewsRequestDto newsRequestDto = createNews();
            newsService.addNews(newsRequestDto);

            //log.info("[{}] 뉴스 생성 완료: {}",
            //        threadName, newsRequestDto.getId());
        } catch (Exception e){
            log.error("뉴스 생성 실패");
        }
    }

    public NewsRequestDto createNews() {
        String threadName = Thread.currentThread().getName();
        long sequence = counter.incrementAndGet();

        return new NewsRequestDto(
                String.format("NEWS-%d-%s", sequence, threadName),
                String.format("뉴스제목-%s", threadName),
                String.format("본문-%s", threadName),
                LocalDateTime.now()
        );
    }
}