package dev.aknb.ordersystem.security;

import dev.aknb.ordersystem.base.ObjectUtils;
import dev.aknb.ordersystem.message.MessageResolver;
import dev.aknb.ordersystem.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final MessageResolver messageResolver;
    private final TokenService tokenService;

    private final String[] WHITE_URLS = new String[]{
            "/api/v1/auth/signup/**",
            "/api/v1/auth/login",
            "/api/v1/auth/password/reset",
            "/api/v1/auth/validate",
            "/api/v1/order/**",
            "/api/v1/country/**",
            "/api/v1/region/**"
    };

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, MessageResolver messageResolver, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.messageResolver = messageResolver;
        this.tokenService = tokenService;
    }

    @Bean
    public AuthenticationManager manager(UserDetailsServiceImpl userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(this.passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_URLS).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, rsp, e) -> {
                            rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            PrintWriter writer = rsp.getWriter();
                            writer.print(ObjectUtils.convertToJson(Response.error(e.getMessage(), HttpStatus.UNAUTHORIZED.name())));
                            writer.flush();
                        }).accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .addFilter(new CustomAuthenticationFilter(manager(userDetailsService), userDetailsService, messageResolver, tokenService))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
