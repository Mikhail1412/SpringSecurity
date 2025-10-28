package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// Create form
@Controller
@RequestMapping("/admin/users")
public class UserController {
    @PostMapping
    public String create(@Valid @ModelAttribute("user") User user, BindingResult binding) {
    return "redirect:/admin/users";
    }
}