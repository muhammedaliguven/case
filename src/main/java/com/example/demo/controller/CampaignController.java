package com.example.demo.controller;

import com.example.demo.aspects.LogExecutionTime;
import com.example.demo.dto.CampaignInformationResponse;
import com.example.demo.entity.Campaign;
import com.example.demo.dto.CampaignRequest;
import com.example.demo.service.CampaignService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/campaign/allStatusChanges")
    @LogExecutionTime
    public ResponseEntity<List<CampaignInformationResponse>> allStatusChanges(@RequestParam(required = false) Campaign.Category category, @RequestParam(required = false) String detail, @RequestParam(required = false) String title) {
        return campaignService.getAllStatusChanges(category, detail, title);
    }

    @PostMapping("/campaign")
    @LogExecutionTime
    public ResponseEntity createCampaign(@Valid @RequestBody CampaignRequest campaignRequest) throws InterruptedException {
        return campaignService.saveCampaign(campaignRequest);
    }

    @PutMapping("/campaign")
    @LogExecutionTime
    public ResponseEntity updateCampaign(@RequestBody CampaignRequest campaignRequest) {
        return campaignService.updateCampaign(campaignRequest);
    }


}
