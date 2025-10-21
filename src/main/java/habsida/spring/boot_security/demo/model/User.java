package habsida.spring.boot_security.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    public static final String NAME_RE = "\\p{L}+(?:[\\s'-]\\p{L}+)*";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 64, message = "Имя не длиннее 64 символов")
    @Pattern(regexp = NAME_RE, message = "Имя: только буквы")
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 64, message = "Фамилия не длиннее 64 символов")
    @Pattern(regexp = NAME_RE, message = "Фамилия: только буквы")
    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 4, max = 255, message = "Пароль от 4 до 255 символов")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}
    public User(String firstName, String lastName, String email, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        if (roles != null) this.roles.addAll(roles);
    }

    public void addRole(Role role) {
        if (role != null) roles.add(role);
    }

    public void removeRole(Role role) {
        if (role != null) roles.remove(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // UserDetails
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override public String getUsername() {
        return email;
    }
    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User))
            return false;

        User other = (User) o;
        if (id != null && other.id != null)
            return Objects.equals(id, other.id);
        return Objects.equals(email, other.email);
    }

    @Override public int hashCode() {
        return Objects.hash(id != null ? id : email);
    }

    @Override
    public String toString() {
        String rs = roles.stream().map(Role::getAuthority).collect(Collectors.joining(","));
        return "User{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName +
                "', email='" + email + "', roles=" + rs + "}";
    }
}
