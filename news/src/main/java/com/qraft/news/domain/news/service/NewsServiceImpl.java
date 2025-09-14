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
        try{
            newsRepository.save(newsRequestDto.toEntity());
            queue.add(newsRequestDto.getId());
        } catch (Exception e){
            log.error("DB 저장 실패 - 뉴스 ID: {}, 오류: {}", newsRequestDto.getId(), e.getMessage(), e);
        }
    }

    public String getNewsId() {
        return queue.poll();
    }

    public NewsResponseDto getNews(String newsId) throws Exception{
        TranslatedNews translatedNews = newsRepository.findById(newsId);

        if (translatedNews == null) {
            throw new Exception("뉴스를 찾을 수 없습니다: " + newsId);
        }

        return new NewsResponseDto(translatedNews);
    }

    public void broadCastNews(String newsId) {
        try {
            NewsResponseDto newsResponseDto = getNews(newsId);
            simpMessagingTemplate.convertAndSend("/ws/news", newsResponseDto);

        } catch (Exception e) {
            log.error("DB에서 뉴스 조회 실패 - 뉴스 ID: {}, 오류: {}", newsId, e.getMessage(), e);
        }
    }
}
