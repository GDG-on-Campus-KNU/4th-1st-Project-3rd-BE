package GDG4th.personaChat.chat.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "chat_log", timeToLive = 21600)
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Indexed
    private Long userId;

    private String userMBTI;

    private List<Message> messages;

    public Chat(Long userId, String userMBTI, List<Message> messages) {
        this.userId = userId;
        this.userMBTI = userMBTI;
        this.messages = messages;
    }
}
