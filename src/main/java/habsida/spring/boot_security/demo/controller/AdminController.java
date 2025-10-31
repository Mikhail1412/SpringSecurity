package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repositories.RoleRepository;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
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

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    @GetMapping("/users/new")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/add";
    }

    @PostMapping("/users")
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            roles.addAll(roleRepository.findAllById(roleIds));
        }
        if (roles.isEmpty()) {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
            roles.add(roleUser);
        }
        u.setRoles(roles);

        userService.save(u);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/edit";
    }

    @PostMapping("/users/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam(required = false) String password,
                         @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        User u = userService.findById(id);
        if (u == null) {
            return "redirect:/admin/users";
        }

        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);

        if (password != null && !password.isBlank()) {
            u.setPassword(passwordEncoder.encode(password));
        }

        Set<Role> roles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            roles.addAll(roleRepository.findAllById(roleIds));
        } else {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
            roles.add(roleUser);
        }
        u.setRoles(roles);

        userService.save(u);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
