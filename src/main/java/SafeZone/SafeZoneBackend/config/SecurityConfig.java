package SafeZone.SafeZoneBackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(frontendUrl, "http://localhost:5173", "http://localhost:5174"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Denuncias: todos los roles autenticados pueden listar/ver
                        // (crear y asignar siguen protegidos con @PreAuthorize en el controller)
                        .requestMatchers("/api/denuncias", "/api/denuncias/**")
                        .hasAnyRole("VICTIM", "ADMIN", "PSYCHOLOGIST", "DEFENDER")

                        // RF-07: Chat — todos los roles autenticados
                        .requestMatchers("/api/mensajes", "/api/mensajes/**")
                        .hasAnyRole("VICTIM", "ADMIN", "PSYCHOLOGIST", "DEFENDER")

                        .requestMatchers("/api/victim/**").hasRole("VICTIM")
                        .requestMatchers("/api/psychologist/**").hasRole("PSYCHOLOGIST")
                        .requestMatchers("/api/defender/**").hasRole("DEFENDER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/agenda/**")
                        .hasAnyRole("VICTIM", "PSYCHOLOGIST", "ADMIN")

                        // RF-05 — Emergencias: roles autenticados; el control fino
                        // por endpoint está en @PreAuthorize del controller.
                        .requestMatchers("/api/emergency/**")
                        .hasAnyRole("VICTIM", "ADMIN", "PSYCHOLOGIST", "DEFENDER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}