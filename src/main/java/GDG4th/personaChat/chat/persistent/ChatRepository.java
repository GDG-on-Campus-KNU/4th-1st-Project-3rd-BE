package GDG4th.personaChat.chat.persistent;

import GDG4th.personaChat.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
