package GDG4th.personaChat.auth.presentation.dto;

public record EmailVerifyRequest(
        String email,
        String code
) {
}
