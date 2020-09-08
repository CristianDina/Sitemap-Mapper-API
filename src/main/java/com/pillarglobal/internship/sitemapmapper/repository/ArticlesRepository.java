package com.pillarglobal.internship.sitemapmapper.repository;


import com.pillarglobal.internship.sitemapmapper.models.db.DbArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesRepository extends JpaRepository<DbArticle, String> {
}
