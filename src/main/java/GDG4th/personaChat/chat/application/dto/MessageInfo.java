package GDG4th.personaChat.chat.application.dto;

import GDG4th.personaChat.chat.domain.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MessageInfo(
    String content, int order, boolean isUserChat, String time
) {
    public static MessageInfo of(Message message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return new MessageInfo(
                message.getContent(),
                message.getOrder(),
                message.isUserChat(),
                message.getTimeStamp().format(formatter)
        );
    }
}
