package com.pillarglobal.internship.sitemapmapper.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
public class SitemapClient {
    private RestTemplate rest=new RestTemplate();
    private String server = "https://www.telegraph.co.uk/sitemap.xml";
    private HttpStatus status;

    public String getChannels(){
        HttpEntity<String> requestEntity = new HttpEntity<String>("");
        ResponseEntity<String> responseEntity = rest.exchange(server, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
