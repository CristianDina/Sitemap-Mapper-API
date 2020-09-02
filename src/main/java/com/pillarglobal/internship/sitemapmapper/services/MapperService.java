package com.pillarglobal.internship.sitemapmapper.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperService {

    @Value("${sitemap.channels}")
    List<String> channels;

    @Scheduled(fixedDelay = 15000)
    public void schedule(){
        System.out.println("scheduled");
    }


}
