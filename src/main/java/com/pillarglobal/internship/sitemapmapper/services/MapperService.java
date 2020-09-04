package com.pillarglobal.internship.sitemapmapper.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pillarglobal.internship.sitemapmapper.clients.SitemapClient;
import com.pillarglobal.internship.sitemapmapper.models.ProcessedSitemap;
import com.pillarglobal.internship.sitemapmapper.models.SitemapItem;
import com.pillarglobal.internship.sitemapmapper.models.db.Sitemap;
import com.pillarglobal.internship.sitemapmapper.repository.MapperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MapperService {
    private SitemapClient sitemapClient;
    private MapperRepository mapperRepository;
    private Logger logger = LoggerFactory.getLogger(MapperService.class);

    @Autowired
    public MapperService(MapperRepository mapperRepository, SitemapClient sitemapClient) {
        this.mapperRepository = mapperRepository;
        this.sitemapClient = sitemapClient;
    }

    @Value("${sitemap.channels}")
    List<String> channels;

    @Scheduled(fixedDelay = 3600000)
    public void schedule() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        ProcessedSitemap processedSitemap =xmlMapper.readValue(sitemapClient.getChannels(), ProcessedSitemap.class);
        updateDb(processedSitemap.getSitemapList());
        logger.info("update complete");
    }


    private void updateDb(List<SitemapItem> list){
        mapperRepository.deleteAll();
        list.forEach(x -> {
            String channel = extractChannel(x);
            Sitemap a = new Sitemap();
            a.setLoc(x.getLoc());
            a.setChannel(channel);
            mapperRepository.save(a);
        });
    }

    private String extractChannel(SitemapItem sitemap){
        String loc = sitemap.getLoc();
        Pattern pattern=Pattern.compile("https://www.telegraph.co.uk/(.*?)/(.*)");
        Matcher matcher = pattern.matcher(loc);
        if (matcher.find())
            return matcher.group(1);
        return "N/A";
    }

}
