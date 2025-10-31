package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repositories.RoleRepository;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // LIST
    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    // ADD FORM
    @GetMapping("/users/new")
    public String addForm(Model model) {
        model.addAttribute("user", new User()); // не обязателен, но ок
        return "admin/add";
    }

    // CREATE
    @PostMapping("/users")
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam String password) {

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password)); // хэш пароля

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        u.setRoles(Set.of(roleUser));

        userService.save(u);
        return "redirect:/admin/users";
    }

    // EDIT FORM
    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User u = userService.findById(id);
        if (u == null) return "redirect:/admin/users";
        model.addAttribute("user", u);
        return "admin/edit";
    }

    // UPDATE
    @PostMapping("/users/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email) {

        User existing = userService.findById(id);
        if (existing == null) return "redirect:/admin/users";

        existing.setFirstName(firstName);
        existing.setLastName(lastName);
        existing.setEmail(email);

        userService.save(existing);
        return "redirect:/admin/users";
    }

    // DELETE
    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
