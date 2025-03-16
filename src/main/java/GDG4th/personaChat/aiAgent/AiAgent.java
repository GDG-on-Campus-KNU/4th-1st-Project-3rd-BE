package GDG4th.personaChat.aiAgent;

import GDG4th.personaChat.chat.domain.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AiAgent {
    public Message messageToAiClient(int order, String content) {
        return new Message("ai's answer", false, order + 1, LocalDateTime.now());
    }
}
