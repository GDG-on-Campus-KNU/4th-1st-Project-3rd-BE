package GDG4th.personaChat.error.presentation;

import GDG4th.personaChat.error.application.ErrorTraceService;
import GDG4th.personaChat.error.application.dto.ErrorTraceInfo;
import GDG4th.personaChat.error.presentation.dto.ErrorTraceResponse;
import GDG4th.personaChat.global.annotation.SessionAdmin;
import GDG4th.personaChat.global.annotation.SessionCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/error")
public class ErrorTraceController {
    private final ErrorTraceService errorTraceService;

    @SessionCheck
    @GetMapping("")
    public ResponseEntity<Page<ErrorTraceResponse>> getAllError(
            @PageableDefault Pageable pageable,
            @SessionAdmin Long id
    ) {
        Page<ErrorTraceResponse> errorTraceResponses = errorTraceService.getAllErrorTrace(pageable)
                .map(ErrorTraceResponse::from);
        return new ResponseEntity<>(errorTraceResponses, HttpStatus.OK);
    }

    @SessionCheck
    @GetMapping("/{id}")
    public ResponseEntity<String> getDetail(
            @PathVariable Long id
    ) {
        String detail = errorTraceService.getDetailById(id);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @SessionCheck
    @DeleteMapping
    public ResponseEntity<Void> deleteAllErrors(
            @SessionAdmin Long id
    ) {
        errorTraceService.deleteAllErrorTrace();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
