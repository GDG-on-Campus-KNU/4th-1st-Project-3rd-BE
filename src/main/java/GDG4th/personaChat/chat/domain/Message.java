package GDG4th.personaChat.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Message implements Serializable {
    private String content;

    private boolean isUserChat;

    private LocalDateTime timeStamp;
}
