package GDG4th.personaChat.error.application;

import GDG4th.personaChat.error.application.dto.ErrorTraceInfo;
import GDG4th.personaChat.error.domain.ErrorTrace;
import GDG4th.personaChat.error.domain.ErrorType;
import GDG4th.personaChat.error.persistent.ErrorTraceRepository;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.ErrorErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ErrorTraceService {
    private final ErrorTraceRepository errorTraceRepository;

    @Transactional(readOnly = true)
    public Page<ErrorTraceInfo> getAllErrorTrace(Pageable pageable) {
        return errorTraceRepository.findAll(pageable).map(ErrorTraceInfo::from);
    }

    @Transactional(readOnly = true)
    public String getDetailById(Long id) {
        ErrorTrace errorTrace = errorTraceRepository.findById(id).orElseThrow(
                () -> CustomException.of(ErrorErrorCode.NOT_FOUND)
        );

        return errorTrace.getDetail();
    }

    @Transactional
    public void saveErrorTrace(Exception e, String instance) {
        ErrorTrace errorTrace;
        if(e.getClass().equals(CustomException.class)) {
             errorTrace = new ErrorTrace(
                 instance,
                 ((CustomException) e).getErrorCode(),
                 ((CustomException) e).getErrorMessage(),
                 ErrorType.EXPECTED,
                 LocalDateTime.now()
             );
             errorTraceRepository.save(errorTrace);
             return;
        }

        errorTrace = new ErrorTrace(
                "E999",
                instance,
                Arrays.toString(e.getStackTrace()),
                ErrorType.UNEXPECTED,
                LocalDateTime.now()
        );
        errorTraceRepository.save(errorTrace);
    }

    @Transactional
    public void deleteAllErrorTrace() {
        errorTraceRepository.deleteAll();
    }
}
