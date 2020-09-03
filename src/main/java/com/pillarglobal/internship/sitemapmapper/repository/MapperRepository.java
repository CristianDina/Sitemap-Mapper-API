package com.pillarglobal.internship.sitemapmapper.repository;

import com.pillarglobal.internship.sitemapmapper.models.db.Sitemap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapperRepository extends JpaRepository<Sitemap, String> {
}
