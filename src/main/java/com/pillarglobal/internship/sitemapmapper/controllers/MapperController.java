package com.pillarglobal.internship.sitemapmapper.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pillarglobal.internship.sitemapmapper.models.DeleteBody;
import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.lang.String.format;


@Controller
@EnableSwagger2
public class MapperController {
    @Autowired
    private MapperService mapperService;
    private Logger logger = LoggerFactory.getLogger(MapperController.class);

    @PostMapping("/triggerSitemapMapping")
    public ResponseEntity<String> triggerSitemapMapping(){
        Thread th = new Thread() {
            public synchronized void run() {
                try{
                    mapperService.startChannelMapping();
                }
                catch(JsonProcessingException e){
                    logger.error("ERROR: Channel Mapping Failed with exception: " + e);
                }
            }
        };
            th.start();
            return new ResponseEntity<>("Channel mapping scheduled.", HttpStatus.OK);
    }

    @PostMapping("/triggerNewsMapping")
    public ResponseEntity<String> triggerNewsMapping(){
            Thread th = new Thread() {
                public synchronized void run() {
                    try{
                        mapperService.startNewsMapping();
                    }
                    catch(JsonProcessingException e){
                        logger.error("ERROR: News Mapping Failed with exception: " + e);
                    }
                }
            };
            th.start();
            return new ResponseEntity<>("News route mapping scheduled", HttpStatus.OK);
    }

    @PostMapping("/triggerNewsCleanup")
    public ResponseEntity<String> triggerNewsCleanup(){
        Thread th = new Thread() {
            public synchronized void run() {
                    mapperService.startNewsCleanup();
            }
        };
        th.start();
        return new ResponseEntity<>("News Cleanup mapping scheduled.", HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteArticle(@RequestBody DeleteBody deleteBody){
        boolean deletedFromNews = false;
        boolean deletedFromArticles = false;
        String articleId = deleteBody.getArticleId();

        if(mapperService.getNews(articleId).isPresent()) {
            mapperService.deleteNews(articleId);
            deletedFromNews = true;
        }
        if(mapperService.getArticle(articleId).isPresent()) {
            mapperService.deleteArticle(articleId);
            deletedFromArticles = true;
        }
        if(deletedFromArticles || deletedFromNews){
            return new ResponseEntity<>(format("Article with id %s was deleted.",articleId), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(format("Article with id %s not found", articleId), HttpStatus.NOT_FOUND);
    }
}
