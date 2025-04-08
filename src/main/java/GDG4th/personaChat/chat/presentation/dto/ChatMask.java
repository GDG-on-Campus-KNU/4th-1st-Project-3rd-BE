package GDG4th.personaChat.chat.presentation.dto;

public record ChatMask(
        int closedMbti
) {
    public static ChatMask of(int closedMbti) {
        return new ChatMask(closedMbti);
    }
}
