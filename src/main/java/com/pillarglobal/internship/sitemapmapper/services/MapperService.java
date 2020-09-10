package com.pillarglobal.internship.sitemapmapper.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pillarglobal.internship.sitemapmapper.clients.SitemapClient;
import com.pillarglobal.internship.sitemapmapper.models.*;

import com.pillarglobal.internship.sitemapmapper.models.db.DbArticle;
import com.pillarglobal.internship.sitemapmapper.models.db.DbNews;
import com.pillarglobal.internship.sitemapmapper.models.db.Sitemap;
import com.pillarglobal.internship.sitemapmapper.repository.ArticlesRepository;
import com.pillarglobal.internship.sitemapmapper.repository.NewsRepository;
import com.pillarglobal.internship.sitemapmapper.repository.SitemapsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import  java.text.SimpleDateFormat;

@Transactional
@Service
public class MapperService {
    private SitemapClient sitemapClient;
    private SitemapsRepository sitemapsRepository;
    private ArticlesRepository articlesRepository;
    private NewsRepository newsRepository;
    private Logger logger = LoggerFactory.getLogger(MapperService.class);
    private XmlMapper xmlMapper = new XmlMapper();
    private volatile boolean isNewsRouteStarted=false;
    private volatile boolean isChannelMappingStarted=false;
    private volatile boolean isCleanupStarted=false;

    @Autowired
    public MapperService( SitemapsRepository sitemapsRepository, SitemapClient sitemapClient, ArticlesRepository articlesRepository, NewsRepository newsRepository) {
        this.sitemapsRepository = sitemapsRepository;
        this.sitemapClient = sitemapClient;
        this.articlesRepository = articlesRepository;
        this.newsRepository = newsRepository;
    }

    @Value("${sitemap.channels}")
    List<String> channels;



    @Scheduled(fixedDelay = 3600000)
    public void startChannelMapping() throws JsonProcessingException {
        if(!isChannelMappingStarted){
            isChannelMappingStarted=true;
            logger.info("Read-Sitemap started");
            ProcessedSitemap processedSitemap = readSitemap();
            logger.info("Update-Sitemap-DB started");
            updateDbSitemap(processedSitemap.getSitemapList());
            logger.info("Read-Channels started");
            ProcessedSitemap weeksAllChannels = processSitemap(processedSitemap);
            logger.info("Read-Articles started");
            ArticleList articleList = extractArticlesFromChannels(weeksAllChannels);
            logger.info("Update-Articles-DB started");
            updateDbArticles(articleList.getArticleList());
            logger.info("Sitemap-Mapping finished");
            isChannelMappingStarted=false;
        }else{
            logger.info("Sitemap Mapping already in progress.");
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void startNewsMapping() throws JsonProcessingException{
        if(!isNewsRouteStarted){
            isNewsRouteStarted=true;
            logger.info("Read-News started");
            UrlSet news = xmlMapper.readValue(sitemapClient.getContent("https://www.telegraph.co.uk/sitemap-news.xml"), UrlSet.class);
            logger.info("Update-News-DB started");
            updateDbNews(news.getUrlList());
            logger.info("Update-News-DB finished");
            isNewsRouteStarted=false;
        }else{
            logger.info("News Mapping already in progress.");
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void startNewsCleanup(){
        if(!isCleanupStarted){
            isCleanupStarted=true;
            logger.info("News Cleanup Started");
            this.newsRepository.deleteOlderNews();
            logger.info("News Cleanup Finished");
            isCleanupStarted=false;
        }else{
            logger.info("News Cleanup already in progress.");
        }

    }

    private ProcessedSitemap readSitemap() throws JsonProcessingException{
        return this.xmlMapper.readValue(sitemapClient.getContent("https://www.telegraph.co.uk/sitemap.xml"), ProcessedSitemap.class);
    }

    private ProcessedSitemap processSitemap(ProcessedSitemap processedSitemap) throws JsonProcessingException{
        ProcessedSitemap weeksAllChannels = new ProcessedSitemap();
        for(SitemapItem s: processedSitemap.getSitemapList()){
            if(channels.contains(extractChannel(s.getLoc())))
            {
                List<SitemapItem> oldList = weeksAllChannels.getSitemapList();
                weeksAllChannels = this.xmlMapper.readValue(sitemapClient.getContent(s.getLoc()),ProcessedSitemap.class);
                if(oldList!=null)
                    weeksAllChannels.setSitemapList(Stream.concat(oldList.stream(),weeksAllChannels.getSitemapList().stream())
                            .collect(Collectors.toList()));
            }
        }
        return weeksAllChannels;
    }

    private ArticleList extractArticlesFromChannels(ProcessedSitemap weeksAllChannels) throws JsonProcessingException{
        ArticleList articleList = new ArticleList();
        for(SitemapItem s:weeksAllChannels.getSitemapList()){
            List<Article> oldList = articleList.getArticleList();
            articleList = this.xmlMapper.readValue(sitemapClient.getContent(s.getLoc()),ArticleList.class);
            if(oldList!=null)
                articleList.setArticleList(Stream.concat(oldList.stream(),articleList.getArticleList().stream())
                        .collect(Collectors.toList()));
        }
        return articleList;
    }

    protected void updateDbSitemap(List<SitemapItem> channels){
//        logger.info("Sitemap repo delete started");
//        sitemapsRepository.deleteAll();
//        logger.info("Sitemap repo delete finished");
        List<Sitemap> sitemapsDb = new ArrayList<>();
        channels.forEach(x -> {
            String channel = extractChannel(x.getLoc());
            Sitemap a = new Sitemap();
            a.setLoc(x.getLoc());
            a.setChannel(channel);
            sitemapsDb.add(a);
        });
        sitemapsRepository.saveAll(sitemapsDb);
    }

    protected void updateDbArticles(List<Article> articles){
//        logger.info("Articles repo delete started");
//        articlesRepository.deleteAll();
//        logger.info("Articles repo delete finished");
        List<DbArticle> articlesDb = new ArrayList<>();
        articles.forEach(x -> {
            String channel = extractChannel(x.getLoc());
            DbArticle a = new DbArticle();
            a.setLoc(x.getLoc());
            a.setChangefreq(x.getChangefreq());
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = new Date();
            try {
                date = format.parse(x.getLastmod());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            a.setLastmod(date);
            a.setPriority(x.getPriority());
            a.setChannel(channel);
            articlesDb.add(a);
        });
        articlesRepository.saveAll(articlesDb);
    }

    protected void updateDbNews(List<Url> urls){
        List<DbNews> newsDb = new ArrayList<>();
        urls.forEach(u -> {
            String channel = extractChannel(u.getLoc());
            DbNews news = new DbNews();
            news.setChannel(channel);
            news.setChangefreq(u.getChangefreq());
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = new Date();
            try {
                date = format.parse(u.getLastmod());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            news.setLastmod(date);
            news.setPriority(u.getPriority());
            news.setLoc(u.getLoc());
            news.setNewsLanguage(u.getNews().getPublication().getLanguage());
            news.setNewsName(u.getNews().getPublication().getName());
            news.setNewsKeywords(u.getNews().getNewsKeywords());
            try {
                date = format.parse(u.getNews().getNewsPublicationDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            news.setNewsPublication_date(date);
            news.setNewsTitle(u.getNews().getNewsTitle());
            newsDb.add(news);
        });
        this.newsRepository.saveAll(newsDb);
    }

    private String extractChannel(String loc){
        Pattern pattern=Pattern.compile("https://www.telegraph.co.uk/(.*?)/(.*)");
        Matcher matcher = pattern.matcher(loc);
        if (matcher.find())
            return matcher.group(1);
        return "N/A";
    }

    public Optional<DbArticle> getArticle(String articleId) {
        return this.articlesRepository.findById(articleId);
    }

    public void deleteArticle(String articleId) {
        this.articlesRepository.deleteById(articleId);

    }

    public Optional<DbNews> getNews(String articleId) {
        return this.newsRepository.findById(articleId);
    }

    public void deleteNews(String articleId) {
        this.newsRepository.deleteById(articleId);
    }
}
