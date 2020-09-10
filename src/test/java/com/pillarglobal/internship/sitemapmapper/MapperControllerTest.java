package com.pillarglobal.internship.sitemapmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillarglobal.internship.sitemapmapper.controllers.MapperController;
import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MapperController.class)
public class MapperControllerTest {
    @MockBean
    MapperService mapperService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void post_triggerSitemapMapping(){

    }
}
