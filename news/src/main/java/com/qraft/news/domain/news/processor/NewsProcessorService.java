package com.qraft.news.domain.news.processor;

import com.qraft.news.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsProcessorService {

    private final NewsService newsService;

    @Async("newsProcessorExecutor")
    public void processNews(String newsId) {
        try {
            String threadName = Thread.currentThread().getName();
            log.info("[{}] 뉴스 처리 시작: {}", threadName, newsId);

            // 실제 처리 로직
            newsService.broadCastNews(newsId);

            log.info("[{}] 뉴스 처리 완료: {}", threadName, newsId);
        } catch (Exception e) {
            log.error("뉴스 처리 실패: {}", newsId, e);
        }
    }
}
