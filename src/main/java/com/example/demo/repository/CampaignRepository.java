package com.example.demo.repository;

import com.example.demo.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByCategoryAndDetailAndTitle(Campaign.Category category, String detail, String title);
    List<Campaign> findByCategoryAndDetailAndTitleAndDuplicateOrderByUpdateDateTimeDesc(Campaign.Category category, String detail, String title,boolean duplicate);
   //Campaign findTopByOrderByUpdateDateTimeDescAndDetailAndCategoryAndTitleAndDuplicate(Campaign.Category category, String detail, String title,boolean duplicate);
    Campaign findTopByOrderByUpdateDateTimeDesc();
    //List<Campaign> findByActiveIsTrue();

}
