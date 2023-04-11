package dev.aknb.ordersystem.config;

import dev.aknb.ordersystem.services.TokenService;
import dev.aknb.ordersystem.services.UserDetailsServiceImpl;
import dev.aknb.ordersystem.utils.ObjectUtils;
import dev.aknb.ordersystem.services.MessageResolverService;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.dtos.security.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final MessageResolverService messageResolverService;
    private final TokenService tokenService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, MessageResolverService messageResolverService, TokenService tokenService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.messageResolverService = messageResolverService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(TokenService.BEARER_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }
        String token = tokenService.getTokenOrElseNull(header);

        if (token == null) {
            setErrorDataToResponse(response, MessageType.EMPTY_CREDENTIALS.name());
            return;
        }

        Optional<Authentication> auth = tokenService.getAuthentication(token);

        if (auth.isEmpty()) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                ((User) auth.get().getPrincipal()).getUsername());

        if (userDetails == null ||
            !tokenService.isIssuedAtAfter(token,
                    ((SecurityUser) userDetails).getPasswordChangedDate())) {

            setErrorDataToResponse(response, MessageType.INVALID_CREDENTIALS.name());
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, auth.get().getCredentials(), auth.get().getAuthorities()));
        chain.doFilter(request, response);
    }

    public void setErrorDataToResponse(HttpServletResponse response, String code, String... args) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                ObjectUtils.convertToJson(
                        Response.error(messageResolverService.getMessage(code, args), code)));
    }
}
