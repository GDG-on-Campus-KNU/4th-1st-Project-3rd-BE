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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Value("${verification.max_chat_length}")
    private static int MAX_CHAT_LENGTH;
    private final ChatCacheRepository chatCacheRepository;
    private final ChatRepository chatRepository;
    private final AiAgent aiAgent;

    @Transactional(readOnly = true)
    public List<MessageInfo> responseMessage(Long userId, int startOrder) {
        if(isFirstChat(userId)) {
            throw CustomException.of(ChatErrorCode.ZERO_CHAT_LOG);
        }

        ChatCache chatCache = chatCacheRepository.findById(userId).orElseThrow(
                () -> CustomException.of(ChatErrorCode.NOT_FOUND)
        );
        List<Message> answer = new ArrayList<>();

        if(chatCache.getLastOrder() < startOrder) {
            return new ArrayList<MessageInfo>();
        }

        if(chatCache.getFirstOrder() > startOrder) {
            Chat chat = chatRepository.findById(userId.toString()).orElseThrow(
                    () -> CustomException.of(ChatErrorCode.NOT_FOUND)
            );

            List<Message> messages = chat.getMessages();
            List<Message> targets = messages.stream()
                    .filter(message -> message.getOrder() >= startOrder)
                    .toList();
            answer.addAll(targets);
        }

        List<Message> targetCache = chatCache.getMessages().stream()
                .filter(message -> message.getOrder() >= startOrder)
                .toList();
        answer.addAll(targetCache);

        List<MessageInfo> responseMessage = answer.stream()
                .map(
                        message -> new MessageInfo(
                                message.getContent(),
                                message.getOrder(),
                                message.isUserChat(),
                                dateFormatting(message.getTimeStamp())
                        )
                )
                .toList();

        return responseMessage.stream().toList();
    }

    private boolean isFirstChat(Long userId) {
        return !chatCacheRepository.existsById(userId) && !chatRepository.existsById(userId.toString());
    }

    private String dateFormatting(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return now.format(formatter);
    }

    public void receiveUserMessage(Long userId, String mbti, String content) {
        ChatCache chatCache = chatCacheRepository.findById(userId).orElseGet(
                () -> new ChatCache(
                        userId,
                        mbti,
                        new ArrayList<>()
                )
        );

        int nextOrder = chatCache.getLastOrder()+1;

        if (chatCache.getMessages().size() == MAX_CHAT_LENGTH) {
            saveChatLogToMongo(chatCache);
            chatCache.clearCache();
        }


        Message message = new Message(content, true, nextOrder, LocalDateTime.now());
        Message aiMessage = aiAgent.messageToAiClient(message.getOrder(), content);
        chatCache.addCache(message);
        chatCache.addCache(aiMessage);

        chatCacheRepository.save(chatCache);
    }

    private void saveChatLogToMongo(ChatCache chatCache) {
        Chat chat = chatRepository.findById(chatCache.getUserId()).orElseGet(
                () -> new Chat(
                        chatCache.getUserId(),
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
}
