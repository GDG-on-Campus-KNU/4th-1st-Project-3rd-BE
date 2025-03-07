package GDG4th.personaChat.global.errorHandling.errorCode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus httpStatus();

    String code();

    String message();
}
