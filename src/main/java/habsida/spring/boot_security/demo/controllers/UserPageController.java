package habsida.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserPageController {

    @GetMapping("/user")
    public String me(Model model, Principal principal) {
        model.addAttribute("email", principal.getName());
        return "user";
    }
}
