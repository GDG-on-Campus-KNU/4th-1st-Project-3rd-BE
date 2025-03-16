package GDG4th.personaChat.chat.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "chat_log")
public class ChatCache implements Serializable {
    @Id
    private String userId;

    private String userMBTI;

    private List<Message> messages;

    public ChatCache(Long userId, String userMBTI, List<Message> messages) {
        this.userId = userId.toString();
        this.userMBTI = userMBTI;
        this.messages = messages;
    }

    public void clearCache() {
        this.messages.clear();
    }

    public void addCache(Message message) {
        this.messages.add(message);
    }

    public int getLastOrder() {
        return messages.isEmpty() ? -1 : messages.get(messages.size()-1).getOrder();
    }
}
