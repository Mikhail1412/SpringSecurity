package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void deleteById(Long id);
}
