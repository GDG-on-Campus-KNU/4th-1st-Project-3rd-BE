package GDG4th.personaChat.chat.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "chat_logs")
public class ChatCache {
    @Id
    private String id;

    private String userId;

    private String role;

    private String text;

    private LocalDateTime timestamp;
}
