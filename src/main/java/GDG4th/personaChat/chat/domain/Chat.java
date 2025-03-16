package GDG4th.personaChat.chat.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "chat_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    private String id;

    private String userMBTI;

    private List<Message> messages;
}
