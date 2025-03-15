package GDG4th.personaChat.global.interceptor;

import GDG4th.personaChat.global.annotation.SessionCheck;
import GDG4th.personaChat.global.errorHandling.CustomException;
import GDG4th.personaChat.global.errorHandling.errorCode.SessionErrorCode;
import GDG4th.personaChat.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isCheckSession(handler)){
            vaildateSession(request, response);
        }
        return true;
    }

    private boolean isCheckSession(Object handler){
        return handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(SessionCheck.class);
    }

    private void vaildateSession(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("userId") == null){
            throw CustomException.of(SessionErrorCode.SESSION_EXPIRED);
        }

        System.out.println("[Session Interceptor] : Interceptor 실행, 세션 유효, 멤버 ID: " + session.getAttribute("userId"));
        CookieUtil.addSessionCookie(response, session.getId());
        request.setAttribute("userId", session.getAttribute("userId"));
    }
}
