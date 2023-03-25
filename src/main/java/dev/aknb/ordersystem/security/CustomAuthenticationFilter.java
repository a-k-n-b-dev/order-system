package dev.aknb.ordersystem.security;

import dev.aknb.ordersystem.base.ObjectUtils;
import dev.aknb.ordersystem.message.MessageResolver;
import dev.aknb.ordersystem.message.MessageType;
import dev.aknb.ordersystem.response.Response;
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
    private final MessageResolver messageResolver;
    private final TokenService tokenService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, MessageResolver messageResolver, TokenService tokenService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.messageResolver = messageResolver;
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

            setErrorDataToResponse(response, MessageType.BAD_CREDENTIALS.name());
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
                        Response.error(messageResolver.getMessage(code, args), code)));
    }
}
