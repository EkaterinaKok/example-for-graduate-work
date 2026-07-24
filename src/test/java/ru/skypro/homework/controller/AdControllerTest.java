package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.service.AdEntityService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdEntityService adEntityService;

    private Ad ad1;
    private Ad ad2;
    private Ads adsResponse;

    @BeforeEach
    void setUp() {
        ad1 = new Ad();
        ad1.setAuthor(1);
        ad1.setImage("/images/test1.jpg");
        ad1.setPk(1);
        ad1.setPrice(1000);
        ad1.setTitle("Тестовое объявление 1");

        ad2 = new Ad();
        ad2.setAuthor(2);
        ad2.setImage("/images/test2.jpg");
        ad2.setPk(2);
        ad2.setPrice(2000);
        ad2.setTitle("Тестовое объявление 2");

        adsResponse = new Ads();
        adsResponse.setCount(2);
        adsResponse.setResults(List.of(ad1, ad2));
    }

    @Test
    void getAllAds_ShouldReturnAds() throws Exception {
        when(adEntityService.getAllAds()).thenReturn(adsResponse);

        mockMvc.perform(get("/ads")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0].title").value("Тестовое объявление 1"))
                .andExpect(jsonPath("$.results[0].price").value(1000))
                .andExpect(jsonPath("$.results[0].image").value("/images/test1.jpg"))
                .andExpect(jsonPath("$.results[1].title").value("Тестовое объявление 2"));
    }

    @Test
    void getAllAds_ShouldReturnEmptyList() throws Exception {
        Ads emptyAds = new Ads();
        emptyAds.setCount(0);
        emptyAds.setResults(List.of());

        when(adEntityService.getAllAds()).thenReturn(emptyAds);

        mockMvc.perform(get("/ads")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.results.length()").value(0));
    }
}
