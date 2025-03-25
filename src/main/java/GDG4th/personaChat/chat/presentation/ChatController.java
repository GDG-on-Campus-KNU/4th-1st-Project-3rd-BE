package GDG4th.personaChat.chat.presentation;

import GDG4th.personaChat.chat.application.ChatService;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.presentation.dto.MessageRequest;
import GDG4th.personaChat.chat.presentation.dto.MessageResponse;
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

    @GetMapping("/{mbti}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getChatLog(
            @RequestParam @Min(0) int startOrder,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        List<MessageInfo> messageInfos = chatService.responseMessage(userId, startOrder);
        List<MessageResponse> messageResponses = messageInfos.stream().map(MessageResponse::of).toList();
        return new ResponseEntity<>(ApiResponse.of(messageResponses), HttpStatus.OK);
    }

    @PostMapping("/{mbti}")
    public ResponseEntity<Void> postChat(
            @RequestBody MessageRequest messageRequest,
            @SessionUserId Long userId,
            @PathVariable String mbti
    ) {
        chatService.receiveUserMessage(userId, "mbti", messageRequest.content());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
