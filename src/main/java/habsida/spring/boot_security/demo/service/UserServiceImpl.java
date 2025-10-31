package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override public List<User> findAll() {
        return repo.findAll();
    }

    @Override public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    @Override public User save(User user) {
        return repo.save(user);
    }

    @Override public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
