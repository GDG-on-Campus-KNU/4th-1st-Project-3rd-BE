package GDG4th.personaChat.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Random;

/**
 * email과 4자리 인증번호를 redis에 저장하는 엔티티
 * verificationCode : 4자리 인증번호
 * failedAttempts : 실패 횟수는 5회를 넘지 말아야 함
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash(value = "EmailVerification", timeToLive = 540) // TTL: 5분
public class EmailVerification implements Serializable {

    @Id
    private String email;

    private String verificationCode;

    private int failedAttempts;

    private static int maxFailedAttempts;

    public static EmailVerification of(String email) {
        return new EmailVerification(email, generateVerificationCode());
    }

    private static String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    private EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.failedAttempts = 0;
    }

    public void plusFailedAttempts() {
        this.failedAttempts++;
    }

    public boolean hasVerificationAttemptsLeft() {
        return failedAttempts < maxFailedAttempts;
    }

    public boolean isValid(String inputCode) {
        return this.verificationCode.equals(inputCode);
    }

    public static void setMaxFailedAttempts(int maxFailedAttempts) {
        EmailVerification.maxFailedAttempts = maxFailedAttempts;
    }
}
