package GDG4th.personaChat.auth.application;

import GDG4th.personaChat.auth.domain.EmailSessionRepository;
import GDG4th.personaChat.auth.presentation.dto.LoginRequest;
import GDG4th.personaChat.auth.presentation.dto.RegisterRequest;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.UserErrorCode;
import GDG4th.personaChat.user.domain.User;
import GDG4th.personaChat.user.persistent.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService verificationService;
    private final EmailSessionRepository emailSessionRepository;

    @Transactional
    public Long register(RegisterRequest request, HttpSession session) {
        String sessionId = session.getId();

        verificationService.verifySession(request.email(), session);

        if(userRepository.existsByEmail(request.email())){
            throw CustomException.of(UserErrorCode.ALREADY_EXIST_USER);
        }

        User user = request.toEntity(passwordEncoder);

        emailSessionRepository.deleteById(sessionId);

        return userRepository.save(user).getId();
    }

    public void login(LoginRequest request, HttpSession session){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> CustomException.of(UserErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw CustomException.of(UserErrorCode.WRONG_PASSWORD);
        }

        session.setAttribute("userId", user.getId());
    }
}
