package GDG4th.personaChat.chat.presentation;

import GDG4th.personaChat.chat.application.ChatFacade;
import GDG4th.personaChat.chat.presentation.dto.*;
import GDG4th.personaChat.global.annotation.SessionCheck;
import GDG4th.personaChat.global.annotation.SessionUserId;
import GDG4th.personaChat.global.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/mbti")
public class ChatController {
    private final ChatFacade chatFacade;

    @SessionCheck
    @GetMapping("/{mbti}")
    public ResponseEntity<ApiResponse<List<ChatResponse>>> getChatLog(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date lastTimestamp,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        List<ChatResponse> chatLog = chatFacade.getChatLog(userId, mbti, lastTimestamp);
        return new ResponseEntity<>(ApiResponse.of(chatLog), HttpStatus.OK);
    }

    @SessionCheck
    @PostMapping("/{mbti}")
    public ResponseEntity<Void> receiveChat(
            @RequestBody ChatRequest chatRequest,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.chatRequest(userId, mbti, chatRequest.content());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @SessionCheck
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<ChatMask>> getClosedChat(
            @SessionUserId Long userId
    ) {
        int userChatMask = chatFacade.getClosedChatRoomMask(userId);
        ChatMask chatMask = ChatMask.of(userChatMask);
        return new ResponseEntity<>(ApiResponse.of(chatMask), HttpStatus.OK);
    }

    @SessionCheck
    @PostMapping("/open")
    public ResponseEntity<Void> openChat(
            @SessionUserId Long userId,
            @RequestBody ChatOpenRequest chatOpenRequest
    ) {
        chatFacade.openNewMbtiChat(userId, chatOpenRequest.mbti());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @SessionCheck
    @DeleteMapping("/{mbti}/close")
    public ResponseEntity<Void> closeChat(
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.closeMbtiChat(userId, mbti);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SessionCheck
    @DeleteMapping("/{mbti}/init")
    public ResponseEntity<Void> resetChat(
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.resetMbtiChat(userId, mbti);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SessionCheck
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<RecentChatLog>>> getRecentChat(
            @SessionUserId Long userId
    ) {
        List<RecentChatLog> recentChat = chatFacade.getRecentChat(userId);
        return new ResponseEntity<>(ApiResponse.of(recentChat), HttpStatus.OK);
    }
}
