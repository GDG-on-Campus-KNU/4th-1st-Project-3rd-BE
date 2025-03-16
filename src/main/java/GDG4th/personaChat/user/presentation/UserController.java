package GDG4th.personaChat.user.presentation;

import GDG4th.personaChat.global.util.ApiResponse;
import GDG4th.personaChat.user.application.UserService;
import GDG4th.personaChat.user.presentation.dto.CheckSessionResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/check-session")
    public ResponseEntity<ApiResponse<CheckSessionResponse>> checkSession(HttpServletRequest request){
        CheckSessionResponse response = CheckSessionResponse.from(userService.checkSession(request));
        return new ResponseEntity<>(ApiResponse.of(response), HttpStatus.OK);
    }
}
