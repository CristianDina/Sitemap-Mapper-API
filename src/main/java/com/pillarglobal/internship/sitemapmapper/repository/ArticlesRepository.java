package com.pillarglobal.internship.sitemapmapper.repository;


import com.pillarglobal.internship.sitemapmapper.models.db.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesRepository extends JpaRepository<Article, String> {
}
