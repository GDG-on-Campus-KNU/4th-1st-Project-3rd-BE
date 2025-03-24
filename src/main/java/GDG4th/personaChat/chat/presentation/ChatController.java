package GDG4th.personaChat.chat.presentation;

import GDG4th.personaChat.chat.application.ChatService;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.presentation.dto.MessageRequest;
import GDG4th.personaChat.chat.presentation.dto.MessageResponse;
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

    // Todo 유저 정보 가져오는 부분 필요
    @GetMapping("/{mbti}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getChatLog(
            @RequestParam @Min(0) int startOrder,
            @PathVariable String mbti
    ) {
        List<MessageInfo> messageInfos = chatService.responseMessage(1L, startOrder);
        List<MessageResponse> messageResponses = messageInfos.stream().map(MessageResponse::of).toList();
        return new ResponseEntity<>(ApiResponse.of(messageResponses), HttpStatus.OK);
    }

    // Todo 유저 정보 가져오는 부분 필요
    @PostMapping("/{mbti}")
    public ResponseEntity<Void> postChat(
            @RequestBody MessageRequest messageRequest,
            @PathVariable String mbti
    ) {
        chatService.receiveUserMessage(1L, "mbti", messageRequest.content());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
