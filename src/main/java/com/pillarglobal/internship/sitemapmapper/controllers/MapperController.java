package com.pillarglobal.internship.sitemapmapper.controllers;

//import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pillarglobal.internship.sitemapmapper.clients.SitemapClient;
import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@Controller
@EnableSwagger2
public class MapperController {
    @Autowired
    private MapperService mapperService;

    @PostMapping("/triggerMapping")
    public ResponseEntity<String> triggerSitemapMapping(){
        try {
            mapperService.schedule();
            return new ResponseEntity<>("scheduled", HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("NOT sched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
