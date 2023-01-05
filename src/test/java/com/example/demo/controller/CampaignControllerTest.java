package com.example.demo.controller;

import com.example.demo.dto.CampaignRequest;
import com.example.demo.entity.Campaign;
import com.example.demo.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demo.entity.Campaign.Status.WAITING_FOR_APPROVAL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
public class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidInputCreateCampaign() throws Exception {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaignRequest.setStatus(WAITING_FOR_APPROVAL);
        mockMvc.perform(post("/api/campaign").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campaignRequest))).andExpect(status().isOk());
    }


    @Test
    void whenInValidInputCreateCampaign() throws Exception {
        mockMvc.perform(post("/api/campaign").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CampaignRequest()))).andExpect(status().isBadRequest());
    }


    @Test
    void whenValidInputUpdateCampaign() throws Exception {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        mockMvc.perform(put("/api/campaign").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campaignRequest))).andExpect(status().isOk());
    }

    @Test
    void whenValidInputAllStatusChanges() throws Exception {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        mockMvc.perform(get("/api/campaign/allStatusChanges").param("category", Campaign.Category.TSS.toString())
                .param("detail", "detailTestdetailTestdetailTest").param("title", "titleTesttitleTesttitleTesttitleTest")).andExpect(status().isOk());
    }


}
