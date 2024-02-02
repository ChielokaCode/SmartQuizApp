package smartquizapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import smartquizapp.enums.Role;

import java.util.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonIgnore
    private Long id;

    @Size(min = 3, message = "First name must be at least 3 characters")
    private String firstName;

    @Size(min = 3, message = "Last name must be at least 3 characters")
    private String lastName;

    @Email(message = "Please enter a valid email address")
    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$", message = "Password must be at least 8 characters and include one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @Transient
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$", message = "Confirm password must match the password")
    private String confirmPassword;

    @NotEmpty(message = "Role cannot be empty")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role userRole;

    @JsonIgnore
    private Boolean isEnabled = false;

    @OneToMany
    @JsonIgnore
    private List<Quiz> quiz;

    @JsonIgnore
    @OneToMany
    private List<Question> Question; //check -error

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(this.userRole.name())));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.isEnabled;
    }
    @JsonIgnore
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching(){
        return password != null && password.equals(confirmPassword);
    }
}

