package smartquizapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import smartquizapp.dto.UserDto;
import smartquizapp.enums.Role;
import smartquizapp.exception.UserNotVerifiedException;
import smartquizapp.model.User;
import smartquizapp.repository.UserRepository;
import smartquizapp.serviceImpl.UserServiceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.function.Function;

@Component
@Slf4j
public class GoogleJwtUtils {

    @Value("${CLIENT_ID}")
    private String CLIENT_ID;

    private UserRepository userRepository;
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    public GoogleJwtUtils(UserRepository userRepository, UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    //Function to take in Google oauth jwt token to return userDto(user details stored with google)
    private final Function<String, UserDto> getUserFromIdToken = (token) -> {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

/////////////From google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))

                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();

//            // Print user identifier
//            String userId = payload.getSubject();

            // Get profile information from payload
            String email = payload.getEmail();
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            if(userRepository.existsByEmail(email)){
                throw new UserNotVerifiedException("User already has an account, pls login");
            }

            // Use or store profile information
            return UserDto.builder()
                    .firstName((givenName == null) ? "NoFirstName" : givenName)
                    .lastName((familyName == null) ? "NoLastName" : familyName)
                    .email(email)

                    //this fields are populated with fixed values
                    // so as not to be left null in database
                    .phoneNumber("00000000000")
                    .password("squad19")
                    .confirmPassword("squad19")
                    .build();

        } else {
            log.info("Invalid ID token.");
        }
        return null;
    };



    //this function takes the UserDto from the function above to
    // register the user into our database and return the jwtToken for that user
    public Function<UserDto, String> saveOAuthUser= (userDto) -> {
      if(userRepository.existsByEmail(userDto.getEmail())){
          UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());
          return jwtUtils.createJwt.apply(userDetails);
      }else {


          User user = new ObjectMapper().convertValue(userDto, User.class);
          user.setIsEnabled(true);
          user.setUserRole(Role.STUDENT);
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
          user = userRepository.save(user);
          return jwtUtils.createJwt.apply(userService.loadUserByUsername(user.getEmail()));
      }
    };


    public String googleOAuthUserJWT(String token){
        UserDto user = getUserFromIdToken.apply(token);
        return saveOAuthUser.apply(user);
    }
}
