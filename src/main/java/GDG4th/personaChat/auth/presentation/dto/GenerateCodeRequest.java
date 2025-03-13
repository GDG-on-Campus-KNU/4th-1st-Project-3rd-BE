package GDG4th.personaChat.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record GenerateCodeRequest(
        @NotBlank String email
) {
}
