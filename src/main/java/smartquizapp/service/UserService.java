package smartquizapp.service;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import smartquizapp.dto.ChangePasswordRequestDto;
import smartquizapp.dto.LoginDto;
import smartquizapp.dto.ResetPasswordDto;
import smartquizapp.model.User;
import smartquizapp.model.VerificationToken;

import java.util.Optional;

public interface UserService {
    String logoutUser(HttpServletRequest request);
    void saveVerificationTokenForUser(User user, String token);
    String logInUser(LoginDto userDto);
    String validateVerificationToken(String token);
    VerificationToken generateNewVerificationToken(String oldToken);
    void createPasswordResetTokenForUser(User user, String token);
    User findUserByEmail(String email);
    String validatePasswordResetToken(String token, ResetPasswordDto passwordDto);
    Optional<User> getUserByPasswordReset(String token);
    void changePassword(User user, String newPassword, String newConfirmPassword);

    String changePasswordForUser(ChangePasswordRequestDto changePasswordDto);
}
