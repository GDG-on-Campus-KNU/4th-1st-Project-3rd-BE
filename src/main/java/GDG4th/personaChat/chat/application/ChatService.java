package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.chat.application.dto.LastChatInfo;
import GDG4th.personaChat.chat.domain.Chat;
import GDG4th.personaChat.chat.persistent.ChatRepository;
import GDG4th.personaChat.chat.presentation.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public void saveChatLog(Long userId, String mbti, String text, String role) {
        Chat chat = new Chat(userId, mbti, text, role);
        chatRepository.save(chat);
    }

    public List<ChatResponse> getAllChatLog(Long userId, String mbti, Date timestamp) {
        List<Chat> chats = chatRepository.findByUserIdAndMbtiAndTimestampAfter(
                userId.toString(), mbti, timestamp
        );
        return chats.stream().map(ChatResponse::of).toList();
    }

    public LastChatInfo getLastChatLog(Long userId, String mbti) {
        Optional<Chat> lastChatLog = chatRepository.findFirstByUserIdAndMbtiOrderByTimestampDesc(userId.toString(), mbti);
        if(lastChatLog.isPresent()) {
            return LastChatInfo.from(lastChatLog.get().getText(), lastChatLog.get().getTimestamp());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.DECEMBER, 31); // 월은 0부터 시작

        return LastChatInfo.from(null, calendar.getTime());
    }

    public void deleteChatLog(Long userId, String mbti) {
        chatRepository.deleteAllByUserIdAndMbti(userId.toString(), mbti);
    }

    public Page<ChatResponse> getAllChatLogByUserId(Long userId , Pageable pageable) {
        return chatRepository.findChatsByUserId(userId.toString(), pageable).map(ChatResponse::of);
    }
}
