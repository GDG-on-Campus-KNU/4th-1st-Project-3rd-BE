package GDG4th.personaChat.admin.presntation.dto;

import GDG4th.personaChat.user.application.dto.UserInfo;

public record AdminUserResponse(
        Long id, String email, String mbti, String role
) {
    public static AdminUserResponse from(UserInfo userInfo) {
        return new AdminUserResponse(
                userInfo.id(),
                userInfo.email(),
                userInfo.mbti(),
                userInfo.role()
        );
    }
}
