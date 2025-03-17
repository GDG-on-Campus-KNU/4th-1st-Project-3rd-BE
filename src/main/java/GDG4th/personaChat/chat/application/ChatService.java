package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.aiAgent.AiAgent;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.domain.ChatCache;
import GDG4th.personaChat.chat.domain.DataSet;
import GDG4th.personaChat.chat.domain.Message;
import GDG4th.personaChat.chat.persistent.ChatCacheRepository;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import GDG4th.personaChat.chat.persistent.DataSetRepository;
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
    private static final int MAX_CHAT_LENGTH = 10;
    private final ChatCacheRepository chatCacheRepository;
    private final ChatRepository chatRepository;
    private final DataSetRepository dataSetRepository;
    private final AiAgent aiAgent;

    @Transactional(readOnly = true)
    public List<MessageInfo> responseMessage(Long userId, int startOrder) {
        // 첫 채팅인지 확인
        if(!chatCacheRepository.existsById(userId) && !chatRepository.existsById(userId.toString())) {
            throw CustomException.of(ChatErrorCode.ZERO_CHAT_LOG);
        }
        ChatCache chatCache = chatCacheRepository.findById(userId).orElseThrow(
                () -> CustomException.of(ChatErrorCode.NOT_FOUND)
        );
        List<Message> answer = new ArrayList<>();

        // 가장 최근이거나 더 큰 값을 요구하는 경우
        if(chatCache.getLastOrder() < startOrder) {
            throw CustomException.of(ChatErrorCode.IS_LATEST);
        }

        // 캐시가 아닌 mongoDB에 있는 채팅 로그의 경우
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

    private String dateFormatting(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return now.format(formatter);
    }

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
            saveChatLogToMongo(chatCache);
            saveDataSetToMongo(chatCache);
            chatCache.clearCache();
        }

        Message message = new Message(content, true, nextOrder, LocalDateTime.now());
        chatCache.addCache(message);
        Message aiMessage = aiAgent.messageToAiClient(message.getOrder(), content);
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

    private void saveDataSetToMongo(ChatCache chatCache) {
        List<Message> messages = chatCache.getMessages();
        for(int i = 0; i < messages.size(); i += 2) {
            String input = messages.get(i).getContent();
            String response = messages.get(i+1).getContent();
            DataSet dataSet = new DataSet(chatCache.getUserMBTI(), input, response);
            dataSetRepository.save(dataSet);
        }
    }
}
