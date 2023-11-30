package rent.tycoon.persistance.config.security.token.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import rent.tycoon.persistance.config.security.token.AccessToken;
import rent.tycoon.persistance.config.security.token.AccessTokenDecoder;
import rent.tycoon.persistance.config.security.token.AccessTokenEncoder;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccessTokenEncoderDecoderImpl implements AccessTokenEncoder, AccessTokenDecoder {
    private final Key key;

    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public String encode(AccessToken accessToken) {
        Map<String, Object> claimsMap = new HashMap<>();

        if(!CollectionUtils.isEmpty(accessToken.getRoles()))
        {
            claimsMap.put("roles", accessToken.getRoles());
        }

        if (accessToken.getUserId() != null)
        {
            claimsMap.put("userId", accessToken.getUserId());
        }

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(accessToken.getSubject())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .addClaims(claimsMap)
                .signWith(key)
                .compact();


    }


    @Override
    public AccessToken decode(String accessTokenEncoded) {
        return null;
    }

}
