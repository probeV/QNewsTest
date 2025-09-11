package com.qraft.news.domain.news.dto.request;

import com.qraft.news.domain.news.TranslatedNews;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class NewsRequestDto {
    private String id;
    private String title;
    private String content;
    private LocalDateTime publishedAt;

    public NewsRequestDto(String id, String title, String content, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publishedAt = publishedAt;
    }

    public TranslatedNews toEntity(){
        return new TranslatedNews(
                id,
                title,
                content,
                publishedAt
        );
    }
}
