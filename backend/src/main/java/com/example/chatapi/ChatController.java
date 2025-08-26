package com.example.chatapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> chat(@RequestBody Map<String,Object> body) {
        logger.info("Received request: {}", body);
        try {
            String message = (String) body.get("message");
            if (message == null) {
                logger.error("Missing message in request");
                return ResponseEntity.badRequest().body(Map.of("error","missing message"));
            }

            String apiKey = System.getenv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                logger.error("OPENAI_API_KEY not set in environment");
                return ResponseEntity.status(500).body(Map.of("error","OPENAI_API_KEY not set in environment"));
            }

            // Build request for the Chat Completions (gpt-4o or gpt-4o-mini etc). Adjust model as needed.
            Map<String,Object> requestPayload = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[] { Map.of("role","user", "content", message) }
            );
            String requestBody = mapper.writeValueAsString(requestPayload);

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                // parse response to extract assistant text (best-effort)
                Map<?,?> json = mapper.readValue(resp.body(), Map.class);
                Object reply = "No reply found";
                try {
                    var choices = (java.util.List<?>) json.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        var first = (Map<?,?>) choices.get(0);
                        var messageObj = (Map<?,?>) first.get("message");
                        if (messageObj != null) reply = messageObj.get("content");
                    }
                } catch (Exception e) {
                    reply = resp.body();
                }
                return ResponseEntity.ok(Map.of("reply", reply));
            } else {
                logger.error("Error from OpenAI API: {}", resp.body());
                return ResponseEntity.status(resp.statusCode()).body(Map.of("error", resp.body()));
            }
        } catch (Exception e) {
            logger.error("Server error: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "server error", "detail", e.getMessage()));
        }
    }
}
