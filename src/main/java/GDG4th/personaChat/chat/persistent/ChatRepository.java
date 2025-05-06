package GDG4th.personaChat.chat.persistent;

import GDG4th.personaChat.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByUserIdAndMbtiAndTimestampAfter(
            String userId, String mbti, Date timestamp
    );

    Optional<Chat> findFirstByUserIdAndMbtiOrderByTimestampDesc(String userId, String mbti);

    void deleteAllByUserIdAndMbti(String userId, String mbti);
}
