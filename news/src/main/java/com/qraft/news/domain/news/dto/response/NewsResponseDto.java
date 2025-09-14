package com.qraft.news.domain.news.dto.response;

import com.qraft.news.domain.news.TranslatedNews;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NewsResponseDto {
    private String id;
    private String title;
    private String content;
    private LocalDateTime publishedAt;

    public NewsResponseDto(TranslatedNews translatedNews) {
        this.id = translatedNews.getId();
        this.title = translatedNews.getTitle();
        this.content = translatedNews.getContent();
        this.publishedAt = translatedNews.getPublishedAt();
    }
}
