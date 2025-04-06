package GDG4th.personaChat.chat.presentation;

import GDG4th.personaChat.chat.application.ChatFacade;
import GDG4th.personaChat.chat.application.ChatService;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.presentation.dto.*;
import GDG4th.personaChat.global.annotation.SessionUserId;
import GDG4th.personaChat.global.util.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/mbti")
public class ChatController {
    private final ChatService chatService;
    private final ChatFacade chatFacade;

    @GetMapping("/{mbti}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getChatLog(
            @RequestParam @Min(0) int startOrder,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        List<MessageInfo> messageInfos = chatService.responseMessage(userId, mbti, startOrder);
        List<MessageResponse> messageResponses = messageInfos.stream().map(MessageResponse::of).toList();
        return new ResponseEntity<>(ApiResponse.of(messageResponses), HttpStatus.OK);
    }

    @PostMapping("/{mbti}")
    public ResponseEntity<Void> receiveChat(
            @RequestBody MessageRequest messageRequest,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.receiveUserMessage(userId, mbti, messageRequest.content());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/open")
    public ResponseEntity<ApiResponse<ChatMask>> getClosedChat(
            @SessionUserId Long userId
    ) {
        int userChatMask = chatFacade.getUserChatMask(userId);
        ChatMask chatMask = ChatMask.of(userChatMask);
        return new ResponseEntity<>(ApiResponse.of(chatMask), HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<RecentChatLog>>> getRecentChat(
            @SessionUserId Long userId
    ) {
        List<RecentChatLog> recentChat = chatFacade.getRecentChat(userId);
        return new ResponseEntity<>(ApiResponse.of(recentChat), HttpStatus.OK);
    }

    @PostMapping("/open")
    public ResponseEntity<Void> openChat(
            @SessionUserId Long userId,
            @RequestBody ChatOpenRequest chatOpenRequest
    ) {
        chatFacade.openNewMbtiChat(userId, chatOpenRequest.mbti());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{mbti}/close")
    public ResponseEntity<Void> closeChat(
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.closeMbtiChat(userId, mbti);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{mbti}/init")
    public ResponseEntity<Void> initChat(
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatFacade.resetChatLog(userId, mbti);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
