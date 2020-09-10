package com.pillarglobal.internship.sitemapmapper.repository;

import com.pillarglobal.internship.sitemapmapper.models.db.DbNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface NewsRepository extends JpaRepository<DbNews, String> {
    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM terrasitemap.news WHERE news_publication_date < DATE(DATE_SUB(NOW(), INTERVAL 2 DAY));",
            nativeQuery = true)
    void deleteOlderNews();

}
