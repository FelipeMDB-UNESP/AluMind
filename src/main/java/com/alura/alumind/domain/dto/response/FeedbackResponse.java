package com.alura.alumind.domain.dto.response;

import com.alura.alumind.domain.enumeration.Sentiment;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    public FeedbackResponse(Sentiment sentiment, List<RequestedFeature> requestedFeatures) {
        this.sentiment = sentiment;
        this.requestedFeatures = requestedFeatures;
    }

    private Long id;
    private Sentiment sentiment;
    private List<RequestedFeature> requestedFeatures;

}
