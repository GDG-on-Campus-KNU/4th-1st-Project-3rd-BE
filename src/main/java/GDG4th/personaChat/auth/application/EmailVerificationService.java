package GDG4th.personaChat.auth.application;

import GDG4th.personaChat.auth.domain.EmailSession;
import GDG4th.personaChat.auth.domain.EmailSessionRepository;
import GDG4th.personaChat.auth.domain.EmailVerification;
import GDG4th.personaChat.auth.domain.EmailVerificationRepository;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.EmailErrorCode;
import GDG4th.personaChat.global.errorHandling.errorCode.UserErrorCode;
import GDG4th.personaChat.user.persistent.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationService {
    private final EmailSendService emailSendService;
    private final EmailVerificationRepository verificationRepository;
    private final EmailSessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Value("${verification.max-failed-attempts}")
    private int maxFailedAttempts;

    @PostConstruct
    public void init(){
        EmailVerification.setMaxFailedAttempts(maxFailedAttempts);
    }

    /**
     * 4자리 인증 코드 생성
     * @param email
     * @throws CustomException
     */
    @Transactional
    public void generateVerificationCode(String email) {
        if(userRepository.existsByEmail(email)){
            throw CustomException.of(UserErrorCode.ALREADY_EXIST_USER);
        }

        EmailVerification verification = EmailVerification.of(email);

        emailSendService.sendMimeMessage(email, verification.getVerificationCode());

        verificationRepository.save(verification);
    }

    /**
     * 4자리 인증 코드 vaild 여부 검토
     * @param email
     * @param inputCode
     * @return
     * @throws CustomException
     */
    @Transactional
    public void verifyCode(String email, String inputCode, HttpSession session){

        // EmailVerification 객체를 찾지 못하면(제한 시간 내에 이메일 인증을 하지 않으면) SESSION_EXPIRED 에러를 발생시킨다.
        EmailVerification verification = verificationRepository.findById(email)
                .orElseThrow(() -> CustomException.of(EmailErrorCode.SEESION_EXPIRED));

        // 인증 시도 횟수가 max_attempts에 도달하면 ERROR_EXCEEDED_ATTEMPTS 에러를 발생시킨다.
        if(!verification.hasVerificationAttemptsLeft()){
            throw CustomException.of(EmailErrorCode.ERROR_EXCEEDED_ATTEMPTS);
        }

        if(!verification.isValid(inputCode)){
            verification.plusFailedAttempts();
            verificationRepository.save(verification);
            throw CustomException.of(EmailErrorCode.ERROR_INVALID_CODE);
        }

        verificationRepository.delete(verification);

        String sessionId = session.getId();

        sessionRepository.save(EmailSession.of(email, sessionId));

    }

    /**
     * 이메일 인증 후 발급되는 세션이 아직 유요한지 검토하는 메서드(TTL : 30분)
     * @param email
     * @throws CustomException
     */
    @Transactional
    public void verifySession(String email, HttpSession session){
        EmailSession emailSession = sessionRepository.findById(email)
                .orElseThrow(() -> CustomException.of(EmailErrorCode.SEESION_EXPIRED));

        // 세션의 이메일과 이메일 세션의 이메일이 일치하지 않으면 SESSION_EMAIL_MISMATCH 에러를 발생시킨다
        if(!Objects.equals(emailSession.getSessionId(), session.getId())){
            throw CustomException.of(EmailErrorCode.SESSION_EMAIL_MISMATCH);
        }

        sessionRepository.delete(emailSession);
    }
}
