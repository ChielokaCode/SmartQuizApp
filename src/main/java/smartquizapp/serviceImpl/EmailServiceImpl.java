package smartquizapp.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import smartquizapp.model.User;
import smartquizapp.model.VerificationToken;

@Component
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("javaspringemailclient@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }



        public String applicationUrl(HttpServletRequest request) {
            return "http://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort() +
                    request.getContextPath();
        }


    public void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/api/v1/user/verifyRegistration?token=" + verificationToken.getToken();


        this.sendSimpleEmail(
                user.getEmail(),
                "Click on your verification link to verify your registration: " + url + ". Link will Expire in 10 minutes.",
                "Verification Token Sent");


    }
    public String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/api/v1/user/savePassword?token=" + token;
        this.sendSimpleEmail(
                user.getEmail(),
                "Enter code into box on your app to reset your Password: " + token + ". Code will Expire in 10 minutes.",
                "Password Reset Code Sent");
        return url;
    }
}

