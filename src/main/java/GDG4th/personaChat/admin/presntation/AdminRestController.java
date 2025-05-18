package GDG4th.personaChat.admin.presntation;

import GDG4th.personaChat.admin.presntation.dto.AdminUserResponse;
import GDG4th.personaChat.chat.application.ChatService;
import GDG4th.personaChat.chat.presentation.dto.ChatResponse;
import GDG4th.personaChat.global.annotation.SessionAdmin;
import GDG4th.personaChat.global.annotation.SessionCheck;
import GDG4th.personaChat.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final UserService userService;
    private final ChatService chatService;

    @SessionCheck
    @GetMapping("/user")
    public ResponseEntity<Page<AdminUserResponse>> getUserData(
            @PageableDefault Pageable pageable,
            @SessionAdmin Long userId
    ) {
        Page<AdminUserResponse> response = userService.getAllUserInfos(pageable).map(AdminUserResponse::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SessionCheck
    @PatchMapping("/user/{id}")
    public ResponseEntity<Void> patchUserData(
            @PathVariable Long id,
            @SessionAdmin Long userId
    ) {
        userService.granting(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SessionCheck
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserData(
            @PathVariable Long id,
            @SessionAdmin Long userId
    ) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SessionCheck
    @GetMapping("/chat/{id}")
    public ResponseEntity<Page<ChatResponse>> getChatData(
            @PathVariable Long id,
            @PageableDefault Pageable pageable
    ) {
        Page<ChatResponse> chatResponses = chatService.getAllChatLogByUserId(id, pageable);
        return new ResponseEntity<>(chatResponses, HttpStatus.OK);
    }
}
