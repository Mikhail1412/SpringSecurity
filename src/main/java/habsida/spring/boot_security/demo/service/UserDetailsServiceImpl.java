package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository users;

    public UserDetailsServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = email
        return users.findByEmail(username)
                .<UsernameNotFoundException>orElseThrow(
                        () -> new UsernameNotFoundException("User not found: " + username)
                );
    }
}
