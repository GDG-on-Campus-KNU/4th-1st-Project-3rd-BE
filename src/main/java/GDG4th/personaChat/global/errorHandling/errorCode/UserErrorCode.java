package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "U001", "Already exist user"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "U002", "Not found user"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "Wrong password");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String code() { return this.code; }

    @Override
    public String message() { return this.message; }
}
