package GDG4th.personaChat.ai.application;

import GDG4th.personaChat.ai.dto.AiRequest;
import GDG4th.personaChat.ai.dto.AiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    @Value("${ai.url}")
    private String aiUrl;
    private final RestTemplate restTemplate;

    public AiResponse messageToAiServer(AiRequest aiRequest) {
        // Blocking
        String uri = aiUrl + "/chat";
        return restTemplate.postForObject(uri, aiRequest, AiResponse.class);
    }
}
