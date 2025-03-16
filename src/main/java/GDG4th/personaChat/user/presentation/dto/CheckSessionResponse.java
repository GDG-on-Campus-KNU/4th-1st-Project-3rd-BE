package GDG4th.personaChat.user.presentation.dto;

public record CheckSessionResponse(
        Boolean isAuthed
) {
    public static CheckSessionResponse from(Boolean isAuthed) {
        return new CheckSessionResponse(isAuthed);
    }
}
