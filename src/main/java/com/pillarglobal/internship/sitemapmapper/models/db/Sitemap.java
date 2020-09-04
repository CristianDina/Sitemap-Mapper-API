package com.pillarglobal.internship.sitemapmapper.models.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "sitemaps", schema = "terrasitemap")
@AllArgsConstructor
@NoArgsConstructor
public class Sitemap {
    @Id
    private String loc;

    private String channel;
}
