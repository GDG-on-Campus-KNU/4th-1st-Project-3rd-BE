package GDG4th.personaChat.user.application.dto;

import GDG4th.personaChat.user.domain.User;

public record UserInfo(
    Long id, String email, String mbti, String role
) {
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getEmail(),
                user.getMbti().name(),
                user.getRole().name()
        );
    }
}
