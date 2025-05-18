package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "No such that error ID");

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