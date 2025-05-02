package GDG4th.personaChat.chat.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "chat_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    private ObjectId id;

    @Field("user_id")
    private String userId;

    @Field("mbti")
    private String mbti;

    @Field("object_id")
    private String objectId;

    @Field("role")
    private String role;

    @Field("text")
    private String text;

    @Field("timestamp")
    private Date timestamp;

    public String changeTimeToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(timestamp);
    }
}
