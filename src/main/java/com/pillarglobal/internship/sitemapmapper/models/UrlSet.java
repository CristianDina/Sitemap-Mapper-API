package com.pillarglobal.internship.sitemapmapper.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "urlset")
public class UrlSet {
    @JacksonXmlProperty(localName = "url")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Url> urlList;
}
