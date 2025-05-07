package GDG4th.personaChat.chat.application.dto;

public record RecentChatLog(
        String mbti,
        LastChatInfo lastChatInfo,
        boolean isViewed
) {
    public static RecentChatLog of(String mbti, LastChatInfo lastChatInfo, boolean isViewed) {
        if(lastChatInfo.chatLog() == null) {
            return new RecentChatLog(mbti, lastChatInfo, true);
        }
        return new RecentChatLog(mbti, lastChatInfo, isViewed);
    }
}
