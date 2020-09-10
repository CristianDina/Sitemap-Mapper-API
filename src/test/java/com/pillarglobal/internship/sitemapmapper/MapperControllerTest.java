package com.pillarglobal.internship.sitemapmapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillarglobal.internship.sitemapmapper.controllers.MapperController;
import com.pillarglobal.internship.sitemapmapper.models.DeleteBody;
import com.pillarglobal.internship.sitemapmapper.models.db.DbArticle;
import com.pillarglobal.internship.sitemapmapper.models.db.DbNews;
import com.pillarglobal.internship.sitemapmapper.services.MapperService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MapperController.class)
public class MapperControllerTest {
    @MockBean
    MapperService mapperService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void post_triggerSitemapMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/triggerSitemapMapping"))
                .andExpect(status().isOk())
                .andExpect(content().string("Channel mapping scheduled."));
        Thread.sleep(1000);
        Mockito.verify(mapperService,Mockito.times(1)).startChannelMapping();
    }

    @Test
    public void post_triggerNewsMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/triggerNewsMapping"))
                .andExpect(status().isOk())
                .andExpect(content().string("News route mapping scheduled"));
        Thread.sleep(1000);
        Mockito.verify(mapperService,Mockito.times(1)).startNewsMapping();
    }

    @Test
    public void post_triggerNewsCleanup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/triggerNewsCleanup"))
                .andExpect(status().isOk())
                .andExpect(content().string("News Cleanup mapping scheduled."));
        Thread.sleep(1000);
        Mockito.verify(mapperService,Mockito.times(1)).startNewsCleanup();
    }

    @Test
    public void delete_deleteArticle() throws Exception {
        String articleId = "testId";
        DeleteBody deleteBody=new DeleteBody();
        deleteBody.setArticleId(articleId);

        Mockito.when(mapperService.getArticle(articleId)).thenReturn(Optional.of(new DbArticle()));
        Mockito.when(mapperService.getNews(articleId)).thenReturn(Optional.of(new DbNews()));


        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(deleteBody));

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Article with id %s was deleted.",articleId)));

        Mockito.verify(mapperService,Mockito.times(1)).deleteNews(articleId);
        Mockito.verify(mapperService,Mockito.times(1)).deleteArticle(articleId);
    }

    @Test
    public void delete_deleteArticle_whenArticleNotFound() throws Exception {
        String articleId = "testId";
        DeleteBody deleteBody=new DeleteBody();
        deleteBody.setArticleId(articleId);

        Mockito.when(mapperService.getArticle(articleId)).thenReturn(Optional.empty());
        Mockito.when(mapperService.getNews(articleId)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(deleteBody));

        mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Article with id %s not found",articleId)));


        Mockito.verify(mapperService,Mockito.times(0)).deleteNews(articleId);
        Mockito.verify(mapperService,Mockito.times(0)).deleteArticle(articleId);
    }
}
