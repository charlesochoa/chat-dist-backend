package chatdist.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTUtils {
    public static String getUsernameFromToken(String token) {
        String user = JWT.require(Algorithm.HMAC512(JWTConstants.secret.getBytes()))
                .build()
                .verify(token.replace(JWTConstants.TOKEN_PREFIX, ""))
                .getSubject();
        if (user != null) {
            return user.toString();
        }
        return null;
    }

    public static String getUserFromToken(String token) {
        String user = JWT.require(Algorithm.HMAC512(JWTConstants.secret.getBytes()))
                .build()
                .verify(token.replace(JWTConstants.TOKEN_PREFIX, ""))
                .getSubject();
        return user;
    }
}
