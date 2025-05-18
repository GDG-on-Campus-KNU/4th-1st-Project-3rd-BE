package GDG4th.personaChat.admin.presntation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/loginPage")
    public String loginPage() {
        return "login.html";
    }

    @GetMapping("/userPage")
    public String userPage() {
        return "user.html";
    }

    @GetMapping("/chatPage")
    public String chatPage() {
        return "chat.html";
    }

    @GetMapping("/errorPage")
    public String errorPage() {
        return "errorPage.html";
    }

    @GetMapping("")
    public String mainPage() {
        return "main.html";
    }
}
