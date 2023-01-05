package com.example.demo.service;

import com.example.demo.dto.CampaignInformationResponse;
import com.example.demo.entity.Campaign;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.dto.CampaignRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;


    public boolean isDuplicate(CampaignRequest campaignRequest) {
        List<Campaign> campaignList = campaignRepository.findByCategoryAndDetailAndTitle(campaignRequest.getCategory(), campaignRequest.getDetail(), campaignRequest.getTitle());
        return campaignList.size() > 0 ? true : false;
    }

    public ResponseEntity saveCampaign(CampaignRequest campaignRequest) {
        try {
            if (isDuplicate(campaignRequest)) {
                campaignRepository.save(createCampaignFromCampaignRequest(campaignRequest, true, true, false));
                return ResponseEntity.status(HttpStatus.CREATED).body("campaign is duplicate");
            }
            campaignRepository.save(createCampaignFromCampaignRequest(campaignRequest, false, true, true));
            return ResponseEntity.status(HttpStatus.CREATED).body("campaign creation successful");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public ResponseEntity updateCampaign(CampaignRequest campaignRequest) {
        try {
            Optional<Campaign> campaignPassive = campaignRepository.findByCategoryAndDetailAndTitleAndDuplicateOrderByUpdateDateTimeDesc(campaignRequest.getCategory(), campaignRequest.getDetail(), campaignRequest.getTitle(), false).stream().findFirst();

            if (campaignPassive.isPresent()) {
                campaignPassive.get().setActive(Boolean.FALSE);
                campaignRepository.saveAll(Arrays.asList(createCampaignFromCampaignRequest(campaignRequest, false, false, true), campaignPassive.get()));
                return ResponseEntity.status(HttpStatus.OK).body("campaign update successful");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("campaign not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Campaign createCampaignFromCampaignRequest(CampaignRequest campaignRequest, Boolean duplicate, Boolean isInitial, Boolean active) {
        Campaign campaign = new Campaign();
        campaign.setCategory(campaignRequest.getCategory());
        campaign.setTitle(campaignRequest.getTitle());
        campaign.setDetail(campaignRequest.getDetail());
        campaign.setActive(active);
        campaign.setDuplicate(duplicate);
        campaign.setStatus(isInitial.equals(Boolean.TRUE) ? calculateStatus(campaignRequest.getCategory()) : campaignRequest.getStatus());
        return campaign;
    }

    public Campaign.Status calculateStatus(Campaign.Category category) {
        switch (category) {
            case OZEL_HAYAT_SIGORTASI:
                return Campaign.Status.ACTIVE;
            default:
                return Campaign.Status.WAITING_FOR_APPROVAL;
        }
    }

    public ResponseEntity<List<CampaignInformationResponse>> getAllStatusChanges(Campaign.Category category, String detail, String title) {
        try {
            List<CampaignInformationResponse> campaignInformationResponses = new ArrayList<>();
            List<Campaign> campaignList = campaignRepository.findByCategoryAndDetailAndTitleAndDuplicateOrderByUpdateDateTimeDesc(category, detail, title, false);
            campaignList.forEach(campaign -> {
                campaignInformationResponses.add(new CampaignInformationResponse(campaign.getStatus(), campaign.getCreateDateTime(), campaign.getUpdateDateTime()));
            });
            return campaignList.size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(campaignInformationResponses) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(campaignInformationResponses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
