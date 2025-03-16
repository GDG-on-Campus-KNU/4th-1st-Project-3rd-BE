package GDG4th.personaChat.global.scheduler;

import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.domain.ChatCache;
import GDG4th.personaChat.chat.domain.Message;
import GDG4th.personaChat.chat.persistent.ChatCacheRepository;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
            Chat chat = chatRepository.findById(cache.getUserId()).orElseGet(
                    () -> new Chat(cache.getUserId(), cache.getUserMBTI(), cache.getMessages())
            );
            // 해당 유저의 첫 로그 저장이 아닌 경우
            if(cache.getMessages().size() > MAX_CHAT_LENGTH) {
                chat.getMessages().addAll(cache.getMessages());
            }
            chatRepository.save(chat);

            cache.clearCache();
            schedulerLogging(cache, lastOrder);
        }
    }

    private void schedulerLogging( ChatCache cache, int lastOrder) {
        Message message1 = new Message(
                "scheduler running!", true, lastOrder + 1, LocalDateTime.now()
        );
        Message message2 = new Message(
                "scheduler running!", false, lastOrder + 2, LocalDateTime.now()
        );
        ChatCache chatCache = new ChatCache(
                Long.parseLong(cache.getUserId()),
                cache.getUserMBTI(),
                List.of(message1, message2)
        );

        chatCacheRepository.save(chatCache);
    }
}
