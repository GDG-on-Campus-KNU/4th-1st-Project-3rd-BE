package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ChatErrorCode implements ErrorCode{
    IS_LATEST(HttpStatus.NO_CONTENT, "C001", "Your message is latest"),
    ZERO_CHAT_LOG(HttpStatus.NO_CONTENT, "C002", "It is your first chatting!"),
    NOT_VALIDATE_PARAM(HttpStatus.BAD_REQUEST, "C003", "Your order value is wrong, order can't be negative value"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C004", "That user's chat log can't find at this system");

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
