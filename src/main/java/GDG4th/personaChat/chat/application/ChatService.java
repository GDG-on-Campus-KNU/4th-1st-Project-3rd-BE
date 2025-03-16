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
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final int MAX_CHAT_LENGTH = 100;
    private final ChatCacheRepository chatCacheRepository;
    private final ChatRepository chatRepository;
    private final AiAgent aiAgent;

    @Transactional(readOnly = true)
    public List<MessageInfo> responseMessage(Long userId, int lastOrder) {
        ChatCache savedChatCache = chatCacheRepository.findById(userId).orElseThrow(
                () -> CustomException.of(ChatErrorCode.NO_CHAT_LOG)
        );
        List<Message> messages = savedChatCache.getMessages();

        // front 메세지가 잘못된 order 값을 사용하는 경우 ( 음수 )
        if(messages.size() < lastOrder || lastOrder < 0) {
            throw CustomException.of(ChatErrorCode.NOT_VALIDATE_PARAM);
        }

        // front 메세지가 가장 최근인 경우
        if(messages.get(messages.size()-1).getOrder() == lastOrder) {
            throw CustomException.of(ChatErrorCode.IS_LATEST);
        }

        List<MessageInfo> responseMessage = messages.stream()
                .map(
                        message -> new MessageInfo(
                                message.getContent(),
                                message.getOrder(),
                                message.isUserChat(),
                                dateFormatting(message.getTimeStamp())
                        )
                )
                .toList();

        return responseMessage.stream().skip(lastOrder).toList();
    }

    private String dateFormatting(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return now.format(formatter);
    }

    @Transactional
    public void receiveUserMessage(Long userId, String mbti, String content) {
        // 채팅 캐시 조회
        ChatCache chatCache = chatCacheRepository.findById(userId).orElseGet(
                () -> new ChatCache(
                        userId,
                        mbti,
                        new ArrayList<>()
                )
        );

        // 마지막 메세지의 순서 + 1
        int nextOrder = chatCache.getLastOrder()+1;

        // 캐시 용량을 벗어났을 때
        if (chatCache.getMessages().size() == MAX_CHAT_LENGTH) {
            saveToMongo(chatCache);
            chatCache.clearCache();
        }

        Message message = new Message(content, true, nextOrder, LocalDateTime.now());
        chatCache.addCache(message);
        Message aiMessage = aiAgent.messageToAiClient(message.getOrder(), content);
        chatCache.addCache(aiMessage);

        chatCacheRepository.save(chatCache);
    }

    private void saveToMongo(ChatCache chatCache) {
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
