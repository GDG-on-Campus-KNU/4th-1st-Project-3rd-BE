package GDG4th.personaChat.ai.application;

import GDG4th.personaChat.ai.dto.AiRequest;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.ChatErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {
    private final WebClient webClient;

    public void messageToAiServer(AiRequest aiRequest) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            String json = mapper.writeValueAsString(aiRequest); // snake_case JSON 생성

            webClient.post()
                    .uri("/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(json) // JSON 문자열로 직접 보냄
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block(); // 동기 호출
            log.info("AI 서버 호출 완료");
        } catch (Exception e) {
            throw CustomException.of(ChatErrorCode.AI_SERVER_ERROR);
        }
    }
}
