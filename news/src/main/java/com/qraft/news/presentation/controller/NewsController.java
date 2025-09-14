package com.qraft.news.presentation.controller;

import com.qraft.news.domain.news.dto.request.NewsRequestDto;
import com.qraft.news.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NewsController {

    private final NewsService newsService;

    /**
     * 실제 SQS 사용 시 활성화될 리스너
     *
     * SQS에서 뉴스 ID를 받아 즉시 브로드캐스트 처리
     */
//    @SqsListener("news-queue")
//    public void receiveFromSqs(@Payload String newsId) {
//        newsService.broadCastNews(newsId);
//    }

}
