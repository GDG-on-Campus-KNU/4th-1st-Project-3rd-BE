package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.domain.Message;
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
    private static final int MAX_CHAT_LENGTH = 50;
    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    public List<MessageInfo> responseMessage(Long userId, int lastOrder) {
        Chat savedChat = chatRepository.findByUserId(userId).orElseThrow(
                () -> CustomException.of(ChatErrorCode.NO_CHAT_LOG)
        );
        List<Message> messages = savedChat.getMessages();

        // front 메세지가 가장 최근인 경우
        if(messages.size() == lastOrder) {
            throw CustomException.of(ChatErrorCode.IS_LATEST);
        }

        // front 메세지가 잘못된 order 값을 사용하는 경우
        if(messages.size() < lastOrder || lastOrder < 0) {
            throw CustomException.of(ChatErrorCode.NOT_VALIDATE_PARAM);
        }

        List<Message> responseMessage = messages.subList(lastOrder, messages.size());

        return responseMessage.stream()
                .map(
                    message -> new MessageInfo(
                            message.getContent(),
                            responseMessage.indexOf(message),
                            message.isUserChat(),
                            dateFormatting(message.getTimeStamp())
                    )
                )
                .toList();
    }

    private String dateFormatting(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return now.format(formatter);
    }

    @Transactional
    public void receiveUserMessage(Long userId, String mbti, String content) {
        // 특정 유저의 채팅을 조회
        Optional<Chat> savedChat = chatRepository.findByUserId(userId);

        // 유저 채팅의 메세지 내용을 조회 ( 없으면 새로운 리스트 생성 )
        List<Message> messages = savedChat.map(Chat::getMessages).orElseGet(ArrayList::new);
        messages.add(
                new Message(content, true, LocalDateTime.now())
        );

        // Todo Trigger 로직 넣기 -> MongoDB로 전송
        if (messages.size() > MAX_CHAT_LENGTH) {
            System.out.println("Mongo DB Inset trigger");
        }

        chatRepository.save(messages.size() == 1 ? new Chat(userId, mbti, messages) : savedChat.get());
    }
}
