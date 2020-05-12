package chatdist.backend.filter;

import chatdist.backend.model.User;
import chatdist.backend.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTConstants {
    public static final String SECRET = "ChatDistBackendSecret";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/auth/sign-up";

//    public static String parseToken(String token) {
//        String username = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
//                .build()
//                .verify(token.replace(TOKEN_PREFIX, ""))
//                .getSubject();
//        return username;
//    }
}
