package GDG4th.personaChat.error.presentation.dto;

import GDG4th.personaChat.error.application.dto.ErrorTraceInfo;

public record ErrorTraceResponse(
        Long id, String instance, String errorCode, String errorType, String timeStamp
) {
    public static ErrorTraceResponse from(ErrorTraceInfo errorTraceInfo) {
        return new ErrorTraceResponse(
                errorTraceInfo.id(),
                errorTraceInfo.instance(),
                errorTraceInfo.errorCode(),
                errorTraceInfo.errorType(),
                errorTraceInfo.timeStamp()
        );
    }
}
