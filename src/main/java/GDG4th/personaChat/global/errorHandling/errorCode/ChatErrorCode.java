package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ChatErrorCode implements ErrorCode{
    LOGICAL_ERROR(HttpStatus.BAD_REQUEST, "C001", "Chat log must be even"),
    ALREADY_OPEN(HttpStatus.BAD_REQUEST, "C002", "It's already opened"),
    ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "C003", "It's already opened"),
    IS_NOT_OPEN_CHAT(HttpStatus.BAD_REQUEST, "C004", "that mbti chat is not opened yet");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
