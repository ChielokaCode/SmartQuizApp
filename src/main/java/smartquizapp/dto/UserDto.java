package smartquizapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    @Size(min = 3, message = "*Enter atleast 3 characters")
    private String firstName;
    @NotNull
    @Size(min = 3, message = "*Enter atleast 3 characters")
    private String lastName;
    @Email(message = "*Enter Proper Email")
    @NotEmpty(message = "*Enter Proper Email")
    private String email;
    @NotNull(message = "*Enter Proper Phone Number")
    private String phoneNumber;
    @Pattern(regexp = "^.*(?=.{8,})(?=...*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "*Enter at least one uppercase,lowercase,digit and special character and minimum 8 characters")
    private String password;
    @Pattern(regexp = "^.*(?=.{8,})(?=...*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "*Enter at least one uppercase,lowercase,digit and special character and minimum 8 characters")
    private String confirmPassword;
}
