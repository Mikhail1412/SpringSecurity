package habsida.spring.boot_security.demo.configs;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repositories.RoleRepository;
import habsida.spring.boot_security.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class BootstrapData {

    @Bean
    CommandLineRunner seedUsers(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            // роли
            Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role user  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));

            // admin (логин admin, пароль admin)
            if (users.findByEmail("admin@local").isEmpty()) {
                User u = new User();
                u.setFirstName("Admin");
                u.setLastName("Root");
                u.setEmail("admin@local");          // это будет username
                u.setPassword(encoder.encode("admin"));
                u.setRoles(Set.of(admin, user));
                users.save(u);
            }

            // user (логин user, пароль user)
            if (users.findByEmail("user@local").isEmpty()) {
                User u = new User();
                u.setFirstName("User");
                u.setLastName("Demo");
                u.setEmail("user@local");           // это будет username
                u.setPassword(encoder.encode("user"));
                u.setRoles(Set.of(user));
                users.save(u);
            }
        };
    }
}
