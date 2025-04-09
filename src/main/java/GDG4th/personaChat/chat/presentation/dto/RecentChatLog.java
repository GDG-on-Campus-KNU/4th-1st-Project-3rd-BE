package GDG4th.personaChat.chat.presentation.dto;

public record RecentChatLog(
        String mbti,
        String lastMessage
) {
    public static RecentChatLog of(String mbti, String lastMessage) {
        if(lastMessage == null || lastMessage.isEmpty()) {
            return new RecentChatLog(mbti, null);
        }
        return new RecentChatLog(mbti, lastMessage);
    }
}
