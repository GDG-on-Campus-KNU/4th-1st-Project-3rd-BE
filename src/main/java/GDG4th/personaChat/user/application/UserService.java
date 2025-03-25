package GDG4th.personaChat.user.application;

import GDG4th.personaChat.user.persistent.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final NicknameGeneratorService nicknameGeneratorService;

    public boolean checkSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }

    public String generateUserNickname(){
        String nickname;
        do {
            nickname = nicknameGeneratorService.generateRandomNickname();
        }while(userRepository.existsByNickname(nickname));

        return nickname;
    }
}
