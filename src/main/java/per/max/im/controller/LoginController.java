package per.max.im.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * Desc
 * </p>
 *
 * @date 2024-09-18
 */
@Controller
public class LoginController {

    @GetMapping({"/index", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat/chat";
    }



}
