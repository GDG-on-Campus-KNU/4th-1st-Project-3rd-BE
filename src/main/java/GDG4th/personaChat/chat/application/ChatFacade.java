package GDG4th.personaChat.chat.application;

import GDG4th.personaChat.chat.presentation.dto.RecentChatLog;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.ChatErrorCode;
import GDG4th.personaChat.user.application.UserService;
import GDG4th.personaChat.user.domain.MBTI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatFacade {
    private final UserService userService;
    private final ChatService chatService;

    public void receiveUserMessage(Long userId, String opponentMbti, String content) {
        String userMbti = userService.getUserMbti(userId);

        if(!isOpened(userId, opponentMbti)) {
            throw CustomException.of(ChatErrorCode.IS_NOT_OPEN_CHAT);
        }

        chatService.saveChatLogToRedis(userId, userMbti, opponentMbti, content);
    }

    private boolean isOpened(Long userId, String opponentMbti) {
        return userService.isOpened(userId, opponentMbti);
    }

    public int getUserChatMask(Long userId) {
         return userService.getUserChatMask(userId);
    }

    public List<RecentChatLog> getRecentChat(Long userId) {
        List<Boolean> openList = userService.getOpenList(userId);
        List<RecentChatLog> recentChatLogs = new ArrayList<>();

        for(int i = 0; i < openList.size(); i++ ) {
            if(openList.get(i)) {
                String mbti = MBTI.values()[i].name();
                String id = userId + ":" + mbti;
                String lastChat = chatService.getLastChat(id);
                boolean isViewed = chatService.isViewed(id);
                recentChatLogs.add(RecentChatLog.of(mbti, lastChat, isViewed));
            }
        }

        return recentChatLogs;
    }

    public void openNewMbtiChat(Long userId, String mbti) {
        if(!userService.openMbtiChat(userId, mbti)) {
            throw CustomException.of(ChatErrorCode.ALREADY_OPEN);
        }
    }

    public void resetChatLog(Long userId, String mbti) {
        String id = userId + ":" + mbti;
        chatService.resetChatLog(id);
    }

    public void closeMbtiChat(Long userId, String mbti) {
        resetChatLog(userId, mbti);

        if(!userService.closeMbtiChat(userId, mbti)) {
            throw CustomException.of(ChatErrorCode.ALREADY_CLOSED);
        }
    }
}
