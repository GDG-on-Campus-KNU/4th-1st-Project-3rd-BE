package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import GDG4th.personaChat.chat.presentation.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public List<ChatResponse> getAllChatLog(Long userId, String mbti, Date timestamp) {
        List<Chat> chats = chatRepository.findByUserIdAndMbtiAndTimestampAfter(
                userId.toString(), mbti, timestamp
        );
        return chats.stream().map(ChatResponse::of).toList();
    }

    public String getLastChatLog(Long userId, String mbti) {
        Optional<Chat> lastChatLog = chatRepository.findFirstByUserIdAndMbtiOrderByTimestampDesc(userId.toString(), mbti);
        if(lastChatLog.isPresent()) {
            return lastChatLog.get().getText();
        }

        return "";
    }

    public void deleteChatLog(Long userId, String mbti) {
        chatRepository.deleteAllByUserIdAndMbti(userId.toString(), mbti);
    }
}
