package com.qraft.news.domain.news;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "TRANSLATED_NEWS")
public class TranslatedNews {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    public TranslatedNews(String id, String title, String content, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publishedAt = publishedAt;
    }

}
