package smartquizapp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import smartquizapp.serviceImpl.UserServiceImpl;
import smartquizapp.utils.JwtAuthenticationFilter;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig implements WebSecurityConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, @Lazy UserServiceImpl userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        // Configure mailSender properties such as host, port, username, password, etc.
        return new JavaMailSenderImpl();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService::loadUserByUsername);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest ->
                        httpRequest
                                .requestMatchers("/api/v1/user/register",
                                        "/api/v1/user/login",
                                        "/api/v1/user/verifyRegistration",
                                        "/api/v1/user/forgotPassword",
                                        "/api/v1/user/resetPassword/{token}",
                                        "/api/v1/user/resendVerifyToken/{oldToken}",
                                        "/api/v1/user/dashboard",
                                        "/api/v1/quiz/all-quiz-images",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/subject/fetch-all-subjects",
                                        "/v3/api-docs.yaml").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/api/v1/user/logout",
                                        "/api/v1/user/changePassword",
                                        "/api/v1/quiz/delete-quiz/{quizId}",
                                        "/api/v1/questions/delete-question/{questionId}",
                                        "/api/v1/quiz/create-quiz",
                                        "/api/v1/quiz/get-quiz/{quizId}",
                                        "/api/v1/quiz/edit-quiz/{id}",
                                        "/api/v1/questions/edit/{questionId}",
                                        "/api/v1/quiz/{id}/publish",
                                        "/api/v1/quiz/get-published-draft",
                                        "/api/v1/quiz/take-quiz/{quizId}",
                                        "/api/v1/quiz/quizzes-by-subject/{subjectName}").authenticated())
                .logout(logout -> logout
                        .deleteCookies("remove")
                        .invalidateHttpSession(true)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Override
    public void init(SecurityBuilder builder) throws Exception {

    }

    @Override
    public void configure(SecurityBuilder builder) throws Exception {

    }
}


