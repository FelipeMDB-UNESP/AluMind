package com.alura.alumind.service;

import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    public String FeedbackInterpreter(String feedback) {

        return "Feedback received!";
    }
}
