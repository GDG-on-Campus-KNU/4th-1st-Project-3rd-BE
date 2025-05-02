package GDG4th.personaChat.ai.dto;

public record AiRequest(
        String userId,
        String mbti,
        String inputText
) {
    public static AiRequest from(String userId, String mbti, String inputText) {
        return new AiRequest(userId, mbti, inputText);
    }
}
