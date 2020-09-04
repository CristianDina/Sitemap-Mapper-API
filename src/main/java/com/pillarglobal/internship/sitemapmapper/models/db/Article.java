package com.pillarglobal.internship.sitemapmapper.models.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Entity
@Table(name = "articles", schema = "terrasitemap")
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    private String loc;
    private String channel;
    private Date lastmod;
    private String changefreq;
    private Float priority;
}
