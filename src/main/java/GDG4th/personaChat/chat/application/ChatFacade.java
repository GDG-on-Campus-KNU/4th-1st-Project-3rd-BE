package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.ai.application.AiService;
import GDG4th.personaChat.ai.dto.AiRequest;
import GDG4th.personaChat.ai.dto.AiResponse;
import GDG4th.personaChat.chat.presentation.dto.ChatResponse;
import GDG4th.personaChat.chat.application.dto.RecentChatLog;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.ChatErrorCode;
import GDG4th.personaChat.user.application.UserService;
import GDG4th.personaChat.user.domain.MBTI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatFacade {
    private final UserService userService;
    private final ChatService chatService;
    private final AiService aiService;

    public void chatRequest(Long userId, String mbti, String text) {
        chatService.saveChatLog(userId, mbti, text, "user");

        AiRequest aiRequest = AiRequest.from(userId.toString(), mbti, text);
        AiResponse aiResponse = aiService.messageToAiServer(aiRequest);
        log.info(aiResponse.response());

        chatService.saveChatLog(userId, mbti, aiResponse.response(), "bot");
        userService.notView(userId, mbti);
    }

    public List<ChatResponse> getChatLog(Long userId, String mbti, Date timestamp) {
        userService.view(userId, mbti);
        return chatService.getAllChatLog(userId, mbti, timestamp);
    }

    public int getClosedChatRoomMask(Long userId) {
        return userService.getUserChatMask(userId);
    }

    public void openNewMbtiChat(Long userId, String mbti) {
        if(!userService.openMbtiChat(userId, mbti)) {
            throw CustomException.of(ChatErrorCode.ALREADY_OPEN);
        }
    }

    public void closeMbtiChat(Long userId, String mbti) {
        if(!userService.closeMbtiChat(userId, mbti)) {
            throw CustomException.of(ChatErrorCode.ALREADY_CLOSED);
        }
        chatService.deleteChatLog(userId, mbti);
    }

    public void resetMbtiChat(Long userId, String mbti) {
        chatService.deleteChatLog(userId, mbti);
    }

    public List<RecentChatLog> getRecentChat(Long userId) {
        List<Boolean> openList = userService.getOpenList(userId);
        List<RecentChatLog> recentChatLogs = new ArrayList<>();
        for (int i = 0; i < openList.size(); i++) {
            if(openList.get(i)) {
                String mbti = MBTI.values()[i].name();
                RecentChatLog recentChatLog = RecentChatLog.of(
                        mbti,
                        chatService.getLastChatLog(userId, mbti),
                        userService.isViewed(userId, mbti)
                );

                recentChatLogs.add(recentChatLog);
            }
        }

        return recentChatLogs.stream()
                .sorted(Comparator.comparing((RecentChatLog log) -> log.lastChatInfo().timestamp()).reversed())
                .collect(Collectors.toList());
    }
}
