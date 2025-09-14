package com.qraft.news.domain.news.service;

import com.qraft.news.domain.news.dto.request.NewsRequestDto;
import com.qraft.news.domain.news.dto.response.NewsResponseDto;

public interface NewsService {

    /**
     * 새로운 뉴스를 DB에 저장하고 처리 대기열에 추가 (SQS 전환 시 사용 x)
     *
     * 1. DB에 뉴스 저장
     * 2. 내부 큐에 뉴스 ID 추가
     *
     * @param newsRequestDto 저장할 뉴스 정보
     */
    public void addNews(NewsRequestDto newsRequestDto);

    /**
     * 내부 큐에서 뉴스 ID를 가져옴 (SQS 전환 시 사용 x)
     *
     * 내부 큐에서 poll()로 즉시 반환 (비블로킹 방식)
     *
     * @return 처리할 뉴스 ID, 큐가 비어있으면 null
     */
    public String getNewsId();

    /**
     * 뉴스 ID로 DB에서 뉴스 상세 정보 조회
     *
     * 1. newsId로 DB 조회
     * 2. TranslatedNews 엔티티를 NewsResponseDto로 변환
     *
     * @param newsId 조회할 뉴스의 고유 ID
     * @return 뉴스 상세 정보 DTO, 존재하지 않으면 null
     */
    public NewsResponseDto getNews(String newsId);

    /**
     * 특정 뉴스 ID의 뉴스를 모든 WebSocket 클라이언트에게 실시간 전송
     *
     * @param newsId 브로드캐스트할 뉴스 ID
     */
    public void broadCastNews(String newsId);


}
