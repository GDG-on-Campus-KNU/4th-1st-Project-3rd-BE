package GDG4th.personaChat.auth.presentation;

import GDG4th.personaChat.auth.application.AuthService;
import GDG4th.personaChat.auth.application.EmailVerificationService;
import GDG4th.personaChat.auth.presentation.dto.*;
import GDG4th.personaChat.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session, HttpServletResponse httpResponse){
        RegisterResponse response = RegisterResponse.from(authService.register(request, session));

        CookieUtil.deleteCookie(httpResponse, "JSESSIONID");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ){
        session.invalidate();

        HttpSession newSession = httpRequest.getSession(true);

        authService.login(request, newSession);

        CookieUtil.addSessionCookie(httpResponse, newSession.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //로그아웃
    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpSession session, HttpServletResponse response){
        CookieUtil.deleteCookie(response, "JSESSIONID");
        session.invalidate();
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
