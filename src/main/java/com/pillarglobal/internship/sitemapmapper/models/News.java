package com.pillarglobal.internship.sitemapmapper.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class News {
    @JacksonXmlProperty(localName = "publication")
    private Publication publication;
    @JacksonXmlProperty(localName = "publication_date")
    private String newsPublicationDate;
    @JacksonXmlProperty(localName = "title")
    private String newsTitle;
    @JacksonXmlProperty(localName = "keywords")
    private String newsKeywords;
}
