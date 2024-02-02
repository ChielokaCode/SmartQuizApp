package smartquizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    private static  final int EXPIRATION_TIME = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //token to be sent to user to enable/verify the user
    private String token;

    //time for token to expire
    private Date expirationTime;

    //one token to one user
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public PasswordResetToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationTime = calculateExpirationDate();
    }

    public PasswordResetToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate();
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        //30 mins form system time
        calendar.add(Calendar.MINUTE, PasswordResetToken.EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}

