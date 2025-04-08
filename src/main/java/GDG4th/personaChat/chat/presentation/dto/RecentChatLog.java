package GDG4th.personaChat.chat.presentation.dto;

public record RecentChatLog(
        String mbti,
        String lastMessage
) {
    public static RecentChatLog of(String mbti, String lastMessage) {
        if(lastMessage.isEmpty() || lastMessage == null) {
            return null;
        }
        return new RecentChatLog(mbti, lastMessage);
    }
}
