package com.example.demo.service;

import com.example.demo.dto.CampaignInformationResponse;
import com.example.demo.dto.CampaignRequest;
import com.example.demo.entity.Campaign;
import com.example.demo.repository.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;

    @Test
    void testIsDuplicate() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        Campaign campaign = new Campaign();
        campaign.setDetail("detailTestdetailTestdetailTest");
        campaign.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign.setCategory(Campaign.Category.TSS);
        List<Campaign> campaignList = Arrays.asList(campaign);

        when(campaignRepository.findByCategoryAndDetailAndTitle(campaignRequest.getCategory(), campaignRequest.getDetail(), campaignRequest.getTitle())).thenReturn(campaignList);
        Boolean isDuplicate = campaignService.isDuplicate(campaignRequest);
        assertEquals(isDuplicate, Boolean.TRUE);
    }

    @Test
    void testCalculateStatus() {
        Campaign.Status status = campaignService.calculateStatus(Campaign.Category.OZEL_HAYAT_SIGORTASI);
        assertEquals(status, Campaign.Status.ACTIVE);
    }

    @Test
    void testCreateCampaignFromCampaignRequest() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        Boolean duplicate=Boolean.FALSE;
        Boolean isInitial=Boolean.TRUE;
        Boolean active =Boolean.TRUE;
        Campaign campaign = campaignService.createCampaignFromCampaignRequest(campaignRequest,duplicate,isInitial,active);
        assertEquals(campaignRequest.getDetail(), campaign.getDetail());
        assertEquals(Campaign.Status.WAITING_FOR_APPROVAL, campaign.getStatus());
        assertEquals(Boolean.TRUE, campaign.isActive());
        assertEquals(Boolean.FALSE, campaign.isDuplicate());
    }

    @Test
    void testGetAllStatusChanges() {
        Campaign.Category category= Campaign.Category.TSS;
        String detail="";
        String title= "";

        Campaign campaign = new Campaign();
        campaign.setDetail("detailTestdetailTestdetailTest");
        campaign.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign.setCategory(Campaign.Category.TSS);
        campaign.setStatus(Campaign.Status.WAITING_FOR_APPROVAL);

        Campaign campaign1 = new Campaign();
        campaign1.setDetail("detailTestdetailTestdetailTest");
        campaign1.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign1.setCategory(Campaign.Category.TSS);
        campaign1.setStatus(Campaign.Status.ACTIVE);

        List<Campaign> campaignList = Arrays.asList(campaign,campaign1);
        when(campaignRepository.findByCategoryAndDetailAndTitleAndDuplicateOrderByUpdateDateTimeDesc(category, detail, title, false)).thenReturn(campaignList);
        ResponseEntity<List<CampaignInformationResponse>> responseEntity = campaignService.getAllStatusChanges(category,detail,title);
        assertEquals(2, responseEntity.getBody().size());
        assertEquals(Campaign.Status.WAITING_FOR_APPROVAL, responseEntity.getBody().get(0).getStatus());
        assertEquals(Campaign.Status.ACTIVE, responseEntity.getBody().get(1).getStatus());

    }


    @Test
    void testSaveCampaignIsNotDuplicate() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        ResponseEntity responseEntity = campaignService.saveCampaign(campaignRequest);
        assertEquals("campaign creation successful", responseEntity.getBody());
    }


    @Test
    void testSaveCampaignIsDuplicate() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        List<Campaign> campaignList=Arrays.asList(new Campaign(),new Campaign());
        when(campaignRepository.findByCategoryAndDetailAndTitle(campaignRequest.getCategory(), campaignRequest.getDetail(), campaignRequest.getTitle())).thenReturn(campaignList);
        ResponseEntity responseEntity = campaignService.saveCampaign(campaignRequest);
        assertEquals("campaign is duplicate", responseEntity.getBody());
    }

    @Test
    void testUpdateCampaignNotFound() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");
        ResponseEntity responseEntity = campaignService.updateCampaign(campaignRequest);
        assertEquals("campaign not found", responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void testUpdateCampaign() {
        Campaign.Category category= Campaign.Category.TSS;
        String detail="detailTestdetailTestdetailTest";
        String title= "titleTesttitleTesttitleTesttitleTest";

        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setCategory(Campaign.Category.TSS);
        campaignRequest.setDetail("detailTestdetailTestdetailTest");
        campaignRequest.setTitle("titleTesttitleTesttitleTesttitleTest");

        Campaign campaign = new Campaign();
        campaign.setDetail("detailTestdetailTestdetailTest");
        campaign.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign.setCategory(Campaign.Category.TSS);
        campaign.setStatus(Campaign.Status.WAITING_FOR_APPROVAL);


        List<Campaign> campaignList = Arrays.asList(campaign);
        when(campaignRepository.findByCategoryAndDetailAndTitleAndDuplicateOrderByUpdateDateTimeDesc(category, detail, title, false)).thenReturn(campaignList);
        ResponseEntity responseEntity = campaignService.updateCampaign(campaignRequest);
        assertEquals("campaign update successful", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}

