package GDG4th.personaChat.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static int DEFAULT_EXPIRY;

    public CookieUtil(@Value("${verification.default_expiry}") int DEFAULT_EXPIRY) {
        CookieUtil.DEFAULT_EXPIRY = DEFAULT_EXPIRY;
    }

    /**
     * 쿠키를 생성하여 응답(Response)에 추가하는 메서드
     *
     * @param response HTTP 응답 객체
     * @param name     쿠키 이름
     * @param value    쿠키 값
     * @param maxAge   쿠키 만료 시간 (초 단위)
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        cookie.setHttpOnly(true); // JavaScript 접근 방지 (XSS 공격 방지)
        cookie.setSecure(false); // HTTPS에서만 쿠키 전송
        cookie.setMaxAge(maxAge); // 만료 시간 설정
        response.addCookie(cookie);
    }

    /**
     * 기본 만료 시간을 사용하여 세션 쿠키를 생성하는 메서드
     *
     * @param response  HTTP 응답 객체
     * @param sessionId 세션 ID 값
     */
    public static void addSessionCookie(HttpServletResponse response, String sessionId) {
        addCookie(response, "JSESSIONID", sessionId, DEFAULT_EXPIRY);
    }

    /**
     * 쿠키를 삭제하는 메서드 (즉시 만료)
     *
     * @param response HTTP 응답 객체
     * @param name     삭제할 쿠키 이름
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        addCookie(response, name, "", 0);
    }

}
