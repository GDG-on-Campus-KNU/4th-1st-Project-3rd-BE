package GDG4th.personaChat.auth.presentation.dto;

import GDG4th.personaChat.user.domain.MBTI;
import GDG4th.personaChat.user.domain.User;
import GDG4th.personaChat.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterRequest(
        @NotBlank String email,
        @NotBlank String password,
        MBTI mbti
){
    public User toEntity(PasswordEncoder passwordEncoder, String nickname){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .mbti(mbti)
                .role(UserRole.USER)
                .build();
    }
}
