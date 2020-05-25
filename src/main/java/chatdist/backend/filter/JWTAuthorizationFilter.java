package chatdist.backend.filter;

import chatdist.backend.config.WebSocketEventListener;
import chatdist.backend.model.CustomUserDetails;
import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import chatdist.backend.util.JWTConstants;
import chatdist.backend.util.JWTUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private UserRepository userRepository;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JWTConstants.HEADER_STRING);

        if (header == null || !header.startsWith(JWTConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWTConstants.HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = JWTUtils.getUserFromToken(token);
            String username = JWTUtils.getUsernameFromToken(token);
            logger.info("Found user {}", username);
            if (user != null && username != null) {
                Optional<User> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isPresent()) {
                    CustomUserDetails customUserDetails = new CustomUserDetails(optionalUser.get());
                    return new UsernamePasswordAuthenticationToken(user, null,
                            customUserDetails.getAuthorities());
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
