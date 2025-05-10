package GDG4th.personaChat.user.application;

import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.UserErrorCode;
import GDG4th.personaChat.user.domain.User;
import GDG4th.personaChat.user.persistent.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public String generateUserNickname(){
        String nickname;
        do {
            nickname = nicknameGeneratorService.generateRandomNickname();
        }while(userRepository.existsByNickname(nickname));

        return nickname;
    }

    @Transactional(readOnly = true)
    public String getUserMbti(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.getMbti().name();
    }

    @Transactional(readOnly = true)
    public int getUserChatMask(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.getOpenedMbti();
    }

    @Transactional(readOnly = true)
    public boolean isOpenedChat(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.isOpened(mbti);
    }

    @Transactional(readOnly = true)
    public List<Boolean> getOpenList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.openingMbti();
    }

    @Transactional
    public boolean openMbtiChat(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.openMbti(mbti);
    }

    @Transactional
    public boolean closeMbtiChat(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.closeMbti(mbti);
    }

    @Transactional
    public void view(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        user.view(mbti);
    }

    @Transactional
    public void notView(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        user.notView(mbti);
    }

    @Transactional(readOnly = true)
    public boolean isViewed(Long userId, String mbti) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.isViewed(mbti);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if(!userRepository.existsById(userId)){
            throw CustomException.of(UserErrorCode.NOT_FOUND_USER);
        }

        userRepository.deleteUserById(userId);
    }

    @Transactional(readOnly = true)
    public String getUserEmail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> CustomException.of(UserErrorCode.NOT_FOUND_USER)
        );

        return user.getEmail();
    }
}
