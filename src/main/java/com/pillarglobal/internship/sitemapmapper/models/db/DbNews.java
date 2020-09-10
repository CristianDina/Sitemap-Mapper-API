package com.pillarglobal.internship.sitemapmapper.models.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "news", schema = "terrasitemap")
@AllArgsConstructor
@NoArgsConstructor
public class DbNews {
    @Id
    private String loc;
    private String channel;
    private Date lastmod;
    private String changefreq;
    private Float priority;
    private String newsName;
    private String newsLanguage;
    private Date newsPublication_date;
    private String newsTitle;
    @Column(columnDefinition = "text")
    private String newsKeywords;
}