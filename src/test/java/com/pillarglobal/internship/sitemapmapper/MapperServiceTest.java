package com.pillarglobal.internship.sitemapmapper;

import com.pillarglobal.internship.sitemapmapper.clients.SitemapClient;
import com.pillarglobal.internship.sitemapmapper.repository.ArticlesRepository;
import com.pillarglobal.internship.sitemapmapper.repository.NewsRepository;
import com.pillarglobal.internship.sitemapmapper.repository.SitemapsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MapperServiceTest {
    SitemapsRepository sitemapsRepository = Mockito.mock(SitemapsRepository.class);
    ArticlesRepository articlesRepository = Mockito.mock(ArticlesRepository.class);
    NewsRepository newsRepository = Mockito.mock(NewsRepository.class);
    SitemapClient sitemapClient = Mockito.mock(SitemapClient.class);

    @Test
    public void test_readSiteMap(){

    }
}
