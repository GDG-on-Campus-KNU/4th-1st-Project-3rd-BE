package GDG4th.personaChat.user.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public boolean checkSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }
}
