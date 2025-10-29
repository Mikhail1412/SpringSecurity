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
public class DevAdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository users,
                                RoleRepository roles,
                                PasswordEncoder encoder) {
        return args -> {
            var adminRole = roles.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            var userRole = roles.findByName("ROLE_USER")
                    .orElseGet(() -> roles.save(new Role("ROLE_USER")));

            users.findByEmail("admin@local").orElseGet(() -> {
                var u = new User();
                u.setFirstName("Admin");
                u.setLastName("Root");
                u.setEmail("admin@local");
                u.setPassword(encoder.encode("1234")); // ВАЖНО: BCrypt
                u.setRoles(Set.of(adminRole, userRole));
                return users.save(u);
            });
        };
    }
}
