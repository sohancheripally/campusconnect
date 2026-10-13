
package com.campusconnect.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final RestClient restClient;
    private final String aiUrl;

    public AssistantController(
            RestClient.Builder builder,
            @Value("${app.ai.url}") String aiUrl) {
        this.restClient = builder.build();
        this.aiUrl = aiUrl;
    }

    @PostMapping("/ask")
    public Map<String, String> askAssistant(
            @RequestBody Map<String, String> request) {

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question cannot be empty");
        }

        try {
            Map<?, ?> response = restClient.post()
                    .uri(aiUrl + "/ask")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("question", question))
                    .retrieve()
                    .body(Map.class);

            Object answer = response == null ? null : response.get("answer");

            return Map.of(
                    "answer",
                    answer == null ? "The AI service returned no answer." : answer.toString()
            );

        } catch (Exception exception) {
            return Map.of(
                    "answer",
                    "The AI assistant is temporarily unavailable. Please try again later."
            );
        }
    }
}
