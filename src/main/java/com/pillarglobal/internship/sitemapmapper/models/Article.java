package com.pillarglobal.internship.sitemapmapper.models;

import lombok.Data;


@Data
public class Article {
    private String loc;
    private String lastmod;
    private String changefreq;
    private Float priority;
}
