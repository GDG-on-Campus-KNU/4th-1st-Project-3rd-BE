package GDG4th.personaChat.chat.presentation.dto;

import GDG4th.personaChat.chat.domain.Chat;

public record ChatResponse(
        String content, boolean isUserChat, String time
) {
    public static ChatResponse of(Chat chat) {
        return new ChatResponse(
                chat.getText(),
                chat.getRole().equals("user"),
                chat.changeTimeToString()
        );
    }
}
