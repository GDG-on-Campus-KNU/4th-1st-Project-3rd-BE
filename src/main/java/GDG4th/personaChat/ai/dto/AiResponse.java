package GDG4th.personaChat.ai.dto;

import java.util.Date;

public record AiResponse(
        String userId,
        String mbti,
        String inputText,
        String response,
        Date responseTimestamp
) {
}
