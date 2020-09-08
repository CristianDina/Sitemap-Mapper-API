package com.pillarglobal.internship.sitemapmapper.repository;

import com.pillarglobal.internship.sitemapmapper.models.db.DbNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<DbNews, String> {
}
