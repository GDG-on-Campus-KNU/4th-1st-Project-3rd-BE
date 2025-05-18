package GDG4th.personaChat.error.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "error_trace")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorTrace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String instance;

    @Column
    private String errorCode;

    @Column
    private String detail;

    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @Column
    private LocalDateTime timestamp;

    public ErrorTrace(String instance, String errorCode, String detail, ErrorType errorType, LocalDateTime timestamp) {
        this.instance = instance;
        this.errorCode = errorCode;
        this.detail = detail;
        this.errorType = errorType;
        this.timestamp = timestamp;
    }
}
