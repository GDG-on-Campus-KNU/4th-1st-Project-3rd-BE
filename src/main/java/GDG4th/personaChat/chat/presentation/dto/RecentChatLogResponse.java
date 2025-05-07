package GDG4th.personaChat.chat.presentation.dto;

import GDG4th.personaChat.chat.application.dto.RecentChatLog;

public record RecentChatLogResponse(
        String mbti,
        String lastMessage,
        boolean isViewed
) {
    public static RecentChatLogResponse of(RecentChatLog recentChatLog) {
        return new RecentChatLogResponse(
                recentChatLog.mbti(), recentChatLog.lastChatInfo().chatLog(), recentChatLog.isViewed()
        );
    }
}
