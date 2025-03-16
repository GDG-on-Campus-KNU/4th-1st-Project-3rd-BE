package GDG4th.personaChat.auth.presentation;

import GDG4th.personaChat.auth.application.AuthService;
import GDG4th.personaChat.auth.application.EmailVerificationService;
import GDG4th.personaChat.auth.presentation.dto.*;
import jakarta.servlet.http.HttpSession;
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
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse response = RegisterResponse.from(authService.register(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request){
        authService.login(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/email/code")
    public ResponseEntity<Void> email(@Valid @RequestBody GenerateCodeRequest request){
        emailVerificationService.generateVerificationCode(request.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/email/code/verify")
    public ResponseEntity<Void> emailVerify(@Valid @RequestBody EmailVerifyRequest request, HttpSession session, HttpServletResponse httpResponse){
        emailVerificationService.verifyCode(request.email(), request.code(), session);

        CookieUtil.addSessionCookie(httpResponse, session.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
