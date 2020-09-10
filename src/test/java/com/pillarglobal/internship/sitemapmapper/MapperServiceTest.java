package com.pillarglobal.internship.sitemapmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pillarglobal.internship.sitemapmapper.clients.SitemapClient;
import com.pillarglobal.internship.sitemapmapper.models.ArticleList;
import com.pillarglobal.internship.sitemapmapper.models.ProcessedSitemap;
import com.pillarglobal.internship.sitemapmapper.models.UrlSet;
import com.pillarglobal.internship.sitemapmapper.repository.ArticlesRepository;
import com.pillarglobal.internship.sitemapmapper.repository.NewsRepository;
import com.pillarglobal.internship.sitemapmapper.repository.SitemapsRepository;
import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;

public class MapperServiceTest {
    SitemapsRepository sitemapsRepository=Mockito.mock(SitemapsRepository.class);;
    ArticlesRepository articlesRepository=Mockito.mock(ArticlesRepository.class);;
    NewsRepository newsRepository=Mockito.mock(NewsRepository.class);
    SitemapClient sitemapClient=Mockito.mock(SitemapClient.class);
    XmlMapper xmlMapper=Mockito.mock(XmlMapper.class);
    MapperService mapperService = new MapperService(sitemapsRepository,sitemapClient,articlesRepository,newsRepository);

    @Test
    public void startChannelMapping_happyPath() throws JsonProcessingException {
        mapperService.setXmlMapper(xmlMapper);
        ProcessedSitemap processedSitemap=new ProcessedSitemap();
        processedSitemap.setSitemapList(new ArrayList<>());
        ArticleList articleList=new ArticleList();
        articleList.setArticleList(new ArrayList<>());

        Mockito.when(sitemapClient.getContent(any())).thenReturn("");
        Mockito.when(sitemapsRepository.saveAll(any())).thenReturn(new ArrayList<>());
        Mockito.when(articlesRepository.saveAll(any())).thenReturn(new ArrayList<>());
        Mockito.when(xmlMapper.readValue("", ProcessedSitemap.class)).thenReturn(processedSitemap);
        Mockito.when(xmlMapper.readValue("", ArticleList.class)).thenReturn(articleList);


        mapperService.startChannelMapping();
        Mockito.verify(sitemapClient,Mockito.times(1)).getContent(any());
        Mockito.verify(sitemapsRepository,Mockito.times(1)).saveAll(any());
        Mockito.verify(articlesRepository,Mockito.times(1)).saveAll(any());
    }

    @Test
    public void startNewsMapping_happyPath() throws JsonProcessingException {
        mapperService.setXmlMapper(xmlMapper);

        UrlSet urlSet=new UrlSet();
        urlSet.setUrlList(new ArrayList<>());

        Mockito.when(sitemapClient.getContent(any())).thenReturn("");
        Mockito.when(newsRepository.saveAll(any())).thenReturn(new ArrayList<>());
        Mockito.when(xmlMapper.readValue("", UrlSet.class)).thenReturn(urlSet);


        mapperService.startNewsMapping();
        Mockito.verify(sitemapClient,Mockito.times(1)).getContent(any());
        Mockito.verify(newsRepository,Mockito.times(1)).saveAll(any());
    }

    @Test
    public void startNewsClear_happyPath() throws JsonProcessingException {
        mapperService.setXmlMapper(xmlMapper);

        Mockito.when(newsRepository.saveAll(any())).thenReturn(new ArrayList<>());

        mapperService.startNewsCleanup();
        Mockito.verify(newsRepository,Mockito.times(1)).deleteOlderNews();
    }
}
