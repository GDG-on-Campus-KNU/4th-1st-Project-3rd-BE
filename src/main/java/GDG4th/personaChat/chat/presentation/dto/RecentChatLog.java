package GDG4th.personaChat.chat.presentation.dto;

public record RecentChatLog(
        String mbti,
        String lastMessage,
        boolean isViewed
) {
    public static RecentChatLog of(String mbti, String lastMessage, boolean isViewed) {
        if(lastMessage == null || lastMessage.isEmpty()) {
            return new RecentChatLog(mbti, null, isViewed);
        }
        return new RecentChatLog(mbti, lastMessage, isViewed);
    }
}
