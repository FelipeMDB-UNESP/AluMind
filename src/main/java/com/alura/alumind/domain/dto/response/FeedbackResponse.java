package com.alura.alumind.domain.dto.response;

import com.alura.alumind.domain.enumeration.Sentiment;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private Sentiment sentiment;
    private List<RequestedFeature> requestedFeatures;
    private String feedbackResponse;

}
