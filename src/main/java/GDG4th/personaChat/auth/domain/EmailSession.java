package GDG4th.personaChat.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash(value = "EmailSession", timeToLive = 1800) // TTL: 30분
public class EmailSession implements Serializable {

    @Id
    private String email;
    private String sessionId;

    public static EmailSession of(String email, String sessionId) {
        return new EmailSession(email, sessionId);
    }

    private EmailSession(String email, String sessionId) {
        this.email = email;
        this.sessionId = sessionId;
    }
}
