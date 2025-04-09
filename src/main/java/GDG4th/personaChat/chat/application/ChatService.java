package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.aiAgent.AiAgent;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.domain.ChatCache;
import GDG4th.personaChat.chat.domain.Message;
import GDG4th.personaChat.chat.persistent.ChatCacheRepository;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final int MAX_CHAT_LENGTH = 100;
    private final ChatCacheRepository chatCacheRepository;
    private final ChatRepository chatRepository;
    private final AiAgent aiAgent;

    @Transactional(readOnly = true)
    public List<MessageInfo> responseMessage(Long userId, String opponentMBTI, int startOrder) {
        String id = userId + ":" + opponentMBTI;
        List<Message> target = findTargetRange(id);
        int size = target.size();

        // case : No chat log in this system
        if(size == 0) {
            return new ArrayList<>();
        }

        // case : startOrder too big
        if(size <= startOrder) {
            return new ArrayList<>();
        }

        // case : normal case
        List<Message> messages = target.subList(startOrder, size);
        return messages.stream().map(MessageInfo::of).toList();
    }

    private List<Message> findTargetRange(String userId) {
        List<Message> target = new ArrayList<>();
        if(!chatRepository.existsById(userId) && chatCacheRepository.existsById(userId)) {
            ChatCache chatCache = chatCacheRepository.findById(userId).get();
            chatCache.statusTrue();
            chatCacheRepository.save(chatCache);
            target.addAll(chatCache.getMessages());
        }
        else if(chatRepository.existsById(userId) && !chatCacheRepository.existsById(userId)) {
            Chat chat = chatRepository.findById(userId).get();
            chat.statusTrue();
            chatRepository.save(chat);
            target.addAll(chat.getMessages());
        }
        else if(chatRepository.existsById(userId) && chatCacheRepository.existsById(userId)) {
            Chat chat = chatRepository.findById(userId).get();
            ChatCache chatCache = chatCacheRepository.findById(userId).get();

            chatCache.statusTrue();
            chat.statusTrue();
            chatCacheRepository.save(chatCache);
            chatRepository.save(chat);

            target.addAll(chat.getMessages());
            target.addAll(chatCache.getMessages());
        }
        return target;
    }

    public void saveChatLogToRedis(Long userId, String userMbti, String opponentMbti, String content) {
        ChatCache chatCache = chatCacheRepository.findById(userId + ":" + opponentMbti).orElseGet(
                () -> new ChatCache(userId, userMbti, opponentMbti, new ArrayList<>())
        );
        chatCache.statusFalse();
        int nextOrder = chatCache.getLastOrder()+1;

        String id = chatCache.getId();
        // case : chat log only exist in persist mongo db
        if(nextOrder == 0 && chatRepository.existsById(id)) {
            nextOrder = chatRepository.findById(id).get().getLastOrder() + 1;
        }

        // case : chat log size reach MAX_LENGTH
        if (chatCache.getMessages().size() == MAX_CHAT_LENGTH) {
            saveChatLogToMongo(chatCache);
            chatCache.clearCache();
        }

        // case : It's Logical Error Case
        if (chatCache.getMessages().size() > MAX_CHAT_LENGTH) {
            throw CustomException.of(ChatErrorCode.LOGICAL_ERROR);
        }

        Message message = new Message(content, true, nextOrder, LocalDateTime.now());
        Message aiMessage = aiAgent.messageToAiClient(nextOrder, content);
        chatCache.addCache(message);
        chatCache.addCache(aiMessage);

        chatCacheRepository.save(chatCache);
    }

    private void saveChatLogToMongo(ChatCache chatCache) {
        Chat chat = chatRepository.findById(chatCache.getId()).orElseGet(
                () -> new Chat(
                        chatCache.getId(),
                        chatCache.isViewed(),
                        chatCache.getUserMBTI(),
                        chatCache.getMessages()
                )
        );

        // 해당 유저의 첫 로그 저장이 아닌 경우
        if(chatCache.getLastOrder() > MAX_CHAT_LENGTH) {
            chat.getMessages().addAll(chatCache.getMessages());
        }

        chatRepository.save(chat);
    }

    public String getLastChat(String id) {
       if(chatCacheRepository.existsById(id)) {
           ChatCache chatCache = chatCacheRepository.findById(id).get();
           Message message = chatCache.getMessages().get(chatCache.getMessages().size() - 1);
           return message.getContent();
       }

       if(chatRepository.existsById(id)) {
           Chat chat = chatRepository.findById(id).get();
           Message message = chat.getMessages().get(chat.getMessages().size());
           return message.getContent();
       }

       return null;
    }

    public void resetChatLog(String id) {
        chatCacheRepository.deleteById(id);
        chatRepository.deleteById(id);
    }

    public boolean isViewed(String id) {
        if(chatCacheRepository.existsById(id)) {
            ChatCache chatCache = chatCacheRepository.findById(id).get();
            return chatCache.isViewed();
        }

        if(chatRepository.existsById(id)) {
            Chat chat = chatRepository.findById(id).get();
            return chat.isViewed();
        }

        return true;
    }
}
