package GDG4th.personaChat.global.errorHandling.errorCode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum EmailErrorCode implements ErrorCode{
    SEESION_EXPIRED(HttpStatus.UNAUTHORIZED, "E001", "Session is expired"),
    ERROR_EXCEEDED_ATTEMPTS(HttpStatus.BAD_REQUEST, "E002", "You have exceeded the number of attempts"),
    ERROR_INVALID_CODE(HttpStatus.BAD_REQUEST, "E003", "Invalid code"),
    SESSION_EMAIL_MISMATCH(HttpStatus.BAD_REQUEST, "E004", "Session email mismatch");

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
