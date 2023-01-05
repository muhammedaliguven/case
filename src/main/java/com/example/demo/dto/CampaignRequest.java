package com.example.demo.dto;

import com.example.demo.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequest {
    @NotNull
    private String title;
    @NotNull
    private String detail;
    @NotNull
    private Campaign.Category category;
    @NotNull
    private Campaign.Status status;
}
