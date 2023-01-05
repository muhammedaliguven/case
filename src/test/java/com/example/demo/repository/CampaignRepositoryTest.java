package com.example.demo.repository;

import com.example.demo.entity.Campaign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CampaignRepositoryTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    public void shouldCreateNewCampaign() {
        Campaign campaign = new Campaign();
        campaign.setDetail("detailTestdetailTestdetailTest");
        campaign.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign.setCategory(Campaign.Category.TSS);
        Campaign campaignSaved = campaignRepository.save(campaign);
        assertThat(campaignSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindCampaignById() {
        Campaign campaign = new Campaign();
        campaign.setDetail("detailTestdetailTestdetailTest");
        campaign.setTitle("titleTesttitleTesttitleTesttitleTest");
        campaign.setCategory(Campaign.Category.TSS);
        campaignRepository.save(campaign);
        Optional<Campaign> campaign1 = campaignRepository.findById(campaign.getId());

        assertThat(campaign1.get().getId()).isGreaterThan(0);
        assertThat(campaign1.get().getDetail()).isEqualTo(campaign.getDetail());

    }


}
