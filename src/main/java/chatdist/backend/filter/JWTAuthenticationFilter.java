package chatdist.backend.filter;

import chatdist.backend.model.CustomUserDetails;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            Optional<User> user = userRepository.findByUsername(credentials.getUsername());
            if (user.isPresent()) {
                CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
                try {
                    Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    credentials.getUsername(),
                                    credentials.getPassword(),
                                    customUserDetails.getAuthorities())
                    );
                    return auth;
                } catch (AuthenticationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new UsernameNotFoundException(credentials.getUsername());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((CustomUserDetails) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTConstants.EXPIRATION_TIME))
                .sign(HMAC512(JWTConstants.SECRET.getBytes()));
        response.addHeader(JWTConstants.HEADER_STRING, JWTConstants.TOKEN_PREFIX + token);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"" + JWTConstants.HEADER_STRING + "\":\"" + JWTConstants.TOKEN_PREFIX + token + "\"}"
        );
    }
}
