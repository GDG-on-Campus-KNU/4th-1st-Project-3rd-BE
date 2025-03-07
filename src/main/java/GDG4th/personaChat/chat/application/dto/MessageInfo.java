package GDG4th.personaChat.chat.application.dto;

public record MessageInfo(
    String content, int order, boolean isUserChat, String time
) {

}
