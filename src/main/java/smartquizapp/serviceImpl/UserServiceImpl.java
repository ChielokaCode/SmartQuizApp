package smartquizapp.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smartquizapp.dto.ChangePasswordRequestDto;
import smartquizapp.dto.LoginDto;
import smartquizapp.dto.ResetPasswordDto;
import smartquizapp.dto.UserDto;
import smartquizapp.enums.Role;
import smartquizapp.exception.EmailIsTakenException;
import smartquizapp.exception.PasswordsDontMatchException;
import smartquizapp.exception.TokenNotFoundException;
import smartquizapp.exception.UserNotFoundException;
import smartquizapp.exception.UserNotVerifiedException;
import smartquizapp.model.PasswordResetToken;
import smartquizapp.model.User;
import smartquizapp.model.VerificationToken;
import smartquizapp.repository.PasswordResetTokenRepository;
import smartquizapp.repository.UserRepository;
import smartquizapp.repository.VerificationTokenRepository;
import smartquizapp.service.UserService;
import smartquizapp.utils.JwtUtils;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Username not Found"));
    }

    public User registerUser(UserDto userDto) {
        User user = new ObjectMapper().convertValue(userDto, User.class);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailIsTakenException("Email is already taken, try Logging In");
        }

        if (user.isPasswordMatching()) {
            user.setUserRole(Role.EDUCATOR);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));

            return userRepository.save(user);
        } else {
            throw new PasswordsDontMatchException("Passwords do not Match!");
        }
    }


    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken newlyCreatedVerificationToken = new VerificationToken(user, token);
        VerificationToken verificationToken = verificationTokenRepository.findByUserId(user.getId());
        if(verificationToken != null){
            verificationTokenRepository.delete(verificationToken);
            verificationTokenRepository.save(newlyCreatedVerificationToken);
        }
        verificationTokenRepository.save(newlyCreatedVerificationToken);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken newlyCreatedPasswordResetToken = new PasswordResetToken(user, token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId());
        if(passwordResetToken != null){
            passwordResetTokenRepository.delete(passwordResetToken);
            passwordResetTokenRepository.save(newlyCreatedPasswordResetToken);
        }
        passwordResetTokenRepository.save(newlyCreatedPasswordResetToken);
    }


    @Override
    public String logInUser(LoginDto userDto) {
        UserDetails user = loadUserByUsername(userDto.getEmail());

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException("User is not verified, check email to Verify Registration");
        }

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UserNotVerifiedException("Username and Password is Incorrect");
        }

        return jwtUtils.createJwt.apply(user);
    }
    @Override
    public String logoutUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);

        if (user != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            return "User logged out Successfully";
        } else {
            return "User not Authenticated";
        }
    }
    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if(verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setIsEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) throws TokenNotFoundException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);

        if (verificationToken == null) {
            throw new TokenNotFoundException("Old token not found");
        }

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }



    @Override
    public String validatePasswordResetToken(String token, ResetPasswordDto passwordDto) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        //password reset token time upon creation is 10min
        //system time(cal) upon password reset token creation starts from 0min
        //so (10-0 is 10min), when it gets to (10min - 10mins(system time) it becomes 0)
        //then it means the password reset token is expired, then delete token from database and return expired
        if (passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        //after checking the token is not invalid(null) and the token is not yet expired
        //then return valid
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordReset(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword, String newConfirmPassword) {

        if (newPassword.equals(newConfirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setConfirmPassword(passwordEncoder.encode(newConfirmPassword));
            userRepository.save(user);
        } else {
            throw new PasswordsDontMatchException("Passwords do not Match!");
        }
    }

    @Override
    public String changePasswordForUser(ChangePasswordRequestDto changePasswordDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
            return "Invalid Old Password";
        }
        if(!changePasswordDto.getNewPassword().equals(changePasswordDto.getOldPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            user.setConfirmPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return "Password changed Successfully";
        }
        return "Old Password and New Password can't be the same";
    }

    public String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

}


