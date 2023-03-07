package cn.ityao.wall.util;

import cn.ityao.wall.service.ITOptionService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * wall-service >>> 【cn.ityao.wall.util】
 * token工具
 *
 * @author: tongyao
 * @since: 2023-02-16
 */
@Component
public class TokenUtil {

    public static String createToken(String userId, String userName, int expireTime, String secret) {
        long EXPIRE_TIME = expireTime * 60 * 1000;
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)

                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret));
        return token;
    }

    public static Map<String, Claim> parsingToken(String token,String secret) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

}
