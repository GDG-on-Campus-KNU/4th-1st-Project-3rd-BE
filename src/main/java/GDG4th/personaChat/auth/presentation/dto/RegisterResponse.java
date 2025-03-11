package GDG4th.personaChat.auth.presentation.dto;

public record RegisterResponse(
        Long userId
) {
    public static RegisterResponse from(Long userId) {
        return new RegisterResponse(userId);
    }
}
