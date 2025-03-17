package GDG4th.personaChat.chat.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor
public class DataSet {
    @Id
    private String id;

    private String mbti;

    private String input;

    private String response;

    public DataSet(String mbti, String input, String response) {
        this.mbti = mbti;
        this.input = input;
        this.response = response;
    }
}
