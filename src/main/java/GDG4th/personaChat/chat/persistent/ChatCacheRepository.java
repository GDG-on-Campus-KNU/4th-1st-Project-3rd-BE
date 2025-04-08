package GDG4th.personaChat.chat.persistent;

import GDG4th.personaChat.chat.domain.ChatCache;
import org.springframework.data.repository.CrudRepository;

public interface ChatCacheRepository extends CrudRepository<ChatCache, String> {
}
