package GDG4th.personaChat.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class NicknameGeneratorService {

    private static final String prefix = "호반우";  // 하나만 사용할 거면 배열 필요 없음
    private static final Random random = new Random();

    public String generateRandomNickname(){
        int number = 10000 + random.nextInt(90000);
        return prefix + number;
    }

}
