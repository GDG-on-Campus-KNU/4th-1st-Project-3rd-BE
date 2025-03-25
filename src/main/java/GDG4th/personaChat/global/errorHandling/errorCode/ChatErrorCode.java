package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ChatErrorCode implements ErrorCode{
    LOGICAL_ERROR(HttpStatus.BAD_REQUEST, "C001", "Chat log must be even");

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
