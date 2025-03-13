package GDG4th.personaChat.auth.application;

import GDG4th.personaChat.auth.presentation.dto.LoginRequest;
import GDG4th.personaChat.auth.presentation.dto.RegisterRequest;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.UserErrorCode;
import GDG4th.personaChat.user.domain.User;
import GDG4th.personaChat.user.persistent.UserRepository;
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

    @Transactional
    public Long register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw CustomException.of(UserErrorCode.ALREADY_EXIST_USER);
        }

        User user = request.toEntity(passwordEncoder);
        return userRepository.save(user).getId();
    }

    public void login(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> CustomException.of(UserErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw CustomException.of(UserErrorCode.WRONG_PASSWORD);
        }
    }
}
