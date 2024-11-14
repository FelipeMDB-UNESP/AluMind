package com.alura.alumind.controller;

import com.alura.alumind.domain.dto.request.FeedbackRequest;
import com.alura.alumind.domain.dto.response.FeedbackResponse;
import com.alura.alumind.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;


    @GetMapping
    public String endpointTest() {

        return "Hello world!";
    }

    @PostMapping
    public FeedbackResponse receiveFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return feedbackService.FeedbackInterpreter(feedbackRequest);
    }
}
