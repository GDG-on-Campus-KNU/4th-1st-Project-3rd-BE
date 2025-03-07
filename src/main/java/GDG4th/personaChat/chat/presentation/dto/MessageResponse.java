package GDG4th.personaChat.chat.presentation.dto;

import GDG4th.personaChat.chat.application.dto.MessageInfo;

import java.time.LocalDateTime;

public record MessageResponse(
        String content, int order, boolean isUserChat, String time
) {
    public static MessageResponse of(MessageInfo messageInfo) {
        return new MessageResponse(
                messageInfo.content(),
                messageInfo.order(),
                messageInfo.isUserChat(),
                messageInfo.time()
        );
    }
}
