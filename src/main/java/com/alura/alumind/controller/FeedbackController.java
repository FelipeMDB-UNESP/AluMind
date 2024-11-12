package com.alura.alumind.controller;

import com.alura.alumind.model.dto.request.FeedbackRequest;
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
    public String receiveFeedback(@RequestBody FeedbackRequest feedbackRequest) {

        return feedbackService.FeedbackInterpreter("Feedback");
    }
}
