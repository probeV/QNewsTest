package com.qraft.news.domain.news.service;

import com.qraft.news.domain.news.TranslatedNews;
import com.qraft.news.domain.news.dto.request.NewsRequestDto;
import com.qraft.news.domain.news.dto.response.NewsResponseDto;
import com.qraft.news.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    /**
     * SQS 시뮬레이션을 위한 내부 큐
     * - LinkedBlockingQueue: 스레드 안전한 큐 구현체
     * - 실제 SQS 전환 시 이 큐는 제거되고, SQS가 메시지 저장소 역할 수행
     */
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NewsRepository newsRepository;

    public void addNews(NewsRequestDto newsRequestDto) {
        newsRepository.save(newsRequestDto.toEntity());
        queue.add(newsRequestDto.getId());
    }

    public String getNewsId() {
        return queue.poll();
    }


    public NewsResponseDto getNews(String newsId) {
        TranslatedNews translatedNews = newsRepository.findById(newsId);

        return new NewsResponseDto(translatedNews);
    }

    public void broadCastNews(String newsId) {
        NewsResponseDto newsResponseDto = getNews(newsId);

        simpMessagingTemplate.convertAndSend("/ws/news", newsResponseDto);
    }
}
