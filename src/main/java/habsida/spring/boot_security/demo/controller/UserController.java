package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("postAction", "/users");
        return "admin/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("postAction", "/users");
            return "admin/user-add";
        }
        userService.save(user);
        return "redirect:/login";
    }
}
