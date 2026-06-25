package br.edu.ifpb.pweb2.eureka.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(reqs -> reqs
            .requestMatchers("/css/**", "/error", "/favicon.ico").permitAll()
            .requestMatchers("/race/create/**", "/race/edit/**", "/race/delete/**", "/question/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            )
        .formLogin(form -> form
            .loginPage("/auth")
            .loginProcessingUrl("/auth")
            .usernameParameter("name")
            .defaultSuccessUrl("/home")
            .failureUrl("/auth")
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/auth/logout"));

    return http.build();
  }

  @Bean
  AuthenticationManager authenticationManager(UserDetailsService service, PasswordEncoder encoder) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(service);
    authProvider.setPasswordEncoder(encoder);

    return new ProviderManager(authProvider);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
