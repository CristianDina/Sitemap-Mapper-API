package com.pillarglobal.internship.sitemapmapper.controllers;

import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Controller
@EnableSwagger2
public class MapperController {
    @Autowired
    private MapperService mapperService;

    @PostMapping()
    public ResponseEntity<String> addArticle(){
        mapperService.schedule();
        return new ResponseEntity<>("scheduled", HttpStatus.OK);
    }
}
