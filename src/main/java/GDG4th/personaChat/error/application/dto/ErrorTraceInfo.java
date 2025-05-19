package GDG4th.personaChat.error.application.dto;

import GDG4th.personaChat.error.domain.ErrorTrace;

public record ErrorTraceInfo(
        Long id, String instance, String errorCode, String errorType, String timeStamp
) {
    public static ErrorTraceInfo from(ErrorTrace errorTrace) {
        return new ErrorTraceInfo(
                errorTrace.getId(),
                errorTrace.getInstance(),
                errorTrace.getErrorCode(),
                errorTrace.getErrorType().name(),
                errorTrace.getTimestamp().toString()
        );
    }
}
