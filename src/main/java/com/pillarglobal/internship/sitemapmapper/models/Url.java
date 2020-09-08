package com.pillarglobal.internship.sitemapmapper.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Url {
    @JacksonXmlProperty(localName = "news")
    private News news;
    private String loc;
    private String lastmod;
    private String changefreq;
    private Float priority;
}
