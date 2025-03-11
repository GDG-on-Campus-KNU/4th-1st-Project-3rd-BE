package GDG4th.personaChat.auth.presentation;

import GDG4th.personaChat.auth.application.CustomOAuth2UserService;
import GDG4th.personaChat.auth.presentation.dto.RegisterRequest;
import GDG4th.personaChat.auth.presentation.dto.RegisterResponse;
import GDG4th.personaChat.user.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse response = RegisterResponse.from(userService.register(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
