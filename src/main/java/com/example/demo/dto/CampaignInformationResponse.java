package com.example.demo.dto;

import com.example.demo.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignInformationResponse {

    private Campaign.Status status;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
