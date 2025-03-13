package GDG4th.personaChat.chat.persistent;

import GDG4th.personaChat.chat.domain.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRepository extends CrudRepository<Chat, Long> {
}
