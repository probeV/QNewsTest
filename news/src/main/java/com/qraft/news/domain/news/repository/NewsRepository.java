package com.qraft.news.domain.news.repository;

import com.qraft.news.domain.news.TranslatedNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<TranslatedNews, Long> {

    TranslatedNews findById(String id);
}
