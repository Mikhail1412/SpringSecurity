package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

    @Controller
    @RequestMapping("/admin/users")
    public class AdminController {
        private final UserService userService;

        public AdminController(UserService userService) {
            this.userService = userService;
        }

        // List
        @GetMapping
        public String list(Model model) {
            model.addAttribute("users", userService.findAll());
            return "users";
        }

        // Create
        @PostMapping
        public String create(@Valid @ModelAttribute("user") User user,
                             BindingResult binding) {
            if (binding.hasErrors()) {
                return "user-form";
            }
            userService.save(user);
            return "redirect:/users";
        }

        // Edit form
        @GetMapping("/{id}/edit")
        public String editForm(@PathVariable Long id, Model model) {
            User user = userService.findById(id);
            if (user == null) {
                return "redirect:/users";
            }
            model.addAttribute("user", user);
            return "user-form";
        }

        // Update
        @PostMapping("/{id}")
        public String update(@PathVariable Long id,
                             @Valid @ModelAttribute("user") User user,
                             BindingResult binding) {
            if (binding.hasErrors()) {
                return "user-form";
            }
            user.setId(id);
            userService.save(user);
            return "redirect:/users";
        }

        // Delete
        @PostMapping("/{id}/delete")
        public String delete(@PathVariable Long id) {
            userService.deleteById(id);
            return "redirect:/users";
        }
    }