package GDG4th.personaChat.chat.application.dto;

import java.util.Date;

public record LastChatInfo(
        String chatLog,
        Date timestamp
) {
    public static LastChatInfo from(String chatLog, Date timestamp) {
        return new LastChatInfo(chatLog, timestamp);
    }
}
