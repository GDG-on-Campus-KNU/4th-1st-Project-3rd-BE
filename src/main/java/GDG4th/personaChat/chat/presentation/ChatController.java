package GDG4th.personaChat.chat.presentation;

import GDG4th.personaChat.chat.application.ChatService;
import GDG4th.personaChat.chat.application.dto.MessageInfo;
import GDG4th.personaChat.chat.presentation.dto.MesageRequest;
import GDG4th.personaChat.chat.presentation.dto.MessageResponse;
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
    public ResponseEntity<?> getChatLog(
            @RequestParam int lastOrder,
            @PathVariable String mbti
    ) {
        List<MessageInfo> messageInfos = chatService.responseMessage(1L, lastOrder);
        List<MessageResponse> messageResponses = messageInfos.stream().map(MessageResponse::of).toList();
        return new ResponseEntity<>(messageResponses, HttpStatus.OK);
    }

    // Todo 유저 정보 가져오는 부분 필요
    @PostMapping("/{mbti}")
    public ResponseEntity<?> postChat(
            @RequestBody MesageRequest mesageRequest,
            @PathVariable String mbti
    ) {
        chatService.receiveUserMessage(1L, "mbti", mesageRequest.content());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
