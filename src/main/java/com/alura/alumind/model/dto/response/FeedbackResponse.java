package com.alura.alumind.model.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {


    private Long id;
    private Enum sentiment;
    private List<RequestedFeature> requestedFeatures;

}
