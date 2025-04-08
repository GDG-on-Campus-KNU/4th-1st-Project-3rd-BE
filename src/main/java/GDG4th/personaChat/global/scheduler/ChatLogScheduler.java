package GDG4th.personaChat.global.scheduler;

import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.domain.ChatCache;
import GDG4th.personaChat.chat.persistent.ChatCacheRepository;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatLogScheduler {
    private static final int MAX_CHAT_LENGTH = 100;
    private final ChatCacheRepository chatCacheRepository;
    private final ChatRepository chatRepository;

    @Scheduled(cron = "0 0 6,12,18,0 * * ?")
    public void scheduledTask() {
        Iterable<ChatCache> caches = chatCacheRepository.findAll();
        for (ChatCache cache : caches) {
            int lastOrder = cache.getLastOrder();

            // mongoDB에 캐시 데이터 저장
            Chat chat = chatRepository.findById(cache.getId()).orElseGet(
                    () -> new Chat(cache.getId(), cache.getUserMBTI(), cache.getMessages())
            );
            // 해당 유저의 첫 로그 저장이 아닌 경우
            if(cache.getMessages().size() > MAX_CHAT_LENGTH) {
                chat.getMessages().addAll(cache.getMessages());
            }
            chatRepository.save(chat);

            cache.clearCache();
        }
    }
}
