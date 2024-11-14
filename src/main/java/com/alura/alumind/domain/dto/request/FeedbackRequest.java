package com.alura.alumind.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    @JsonProperty("feedback")
    private String feedbackText;
}
