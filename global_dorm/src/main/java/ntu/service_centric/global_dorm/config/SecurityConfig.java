package ntu.service_centric.global_dorm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity, enable it in production with proper configurations
                .csrf(csrf -> csrf.disable())

                // Authorization configurations
                .authorizeHttpRequests(auth -> auth
                        // Permit public access to login, registration, and root endpoints
                        .requestMatchers("/login", "/register", "/", "/api/credentials/**").permitAll()

                        // Protect API endpoints under `/applications/api` and `/api/users`
                        .requestMatchers("/applications/api/**", "/api/users/**").authenticated()

                        // Protect all other requests
                        .anyRequest().authenticated()
                )

                // Configure form-based login
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page
                        .defaultSuccessUrl("/dashboard", true)  // Redirect after successful login
                        .failureUrl("/login?error")  // Redirect after login failure
                        .permitAll()  // Allow all users to access the login page
                )

                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Logout URL
                        .logoutSuccessUrl("/login?logout")  // Redirect after logout
                        .invalidateHttpSession(true)  // Invalidate session on logout
                        .clearAuthentication(true)  // Clear authentication on logout
                        .permitAll()  // Allow all users to access the logout functionality
                );

        return http.build();
    }

    // Bean for password encoding using BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for managing authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
