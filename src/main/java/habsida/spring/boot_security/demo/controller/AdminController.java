package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    @GetMapping("/users/new")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("postAction", "/admin/users");
        return "admin/add";
    }

    @PostMapping("/users")
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("postAction", "/admin/users");
            return "admin/add";
        }
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User u = userService.findById(id);
        if (u == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", u);
        return "admin/edit";
    }

    @PostMapping("/users/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("user") User user,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/edit";
        }
        user.setId(id);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
