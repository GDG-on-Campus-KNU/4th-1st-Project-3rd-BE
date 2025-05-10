package GDG4th.personaChat.chat.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Document(collection = "chat_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "user_mbti_time_idx", def = "{'user_id': 1, 'mbti': 1, 'timestamp': 1}")
})
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

    public Chat(Long userId, String mbti, String text, String role) {
        this.userId = userId.toString();
        this.mbti = mbti;
        this.objectId = userId + "_" + mbti;
        this.role = role;
        this.text = text;
        this.timestamp = Date.from(Instant.now());
    }

    public String changeTimeToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(timestamp);
    }
}
