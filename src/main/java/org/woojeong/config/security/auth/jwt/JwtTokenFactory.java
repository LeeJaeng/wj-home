package org.woojeong.config.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.woojeong.config.security.auth.jwt.model.AccessJwtToken;
import org.woojeong.config.security.auth.jwt.model.JwtToken;
import org.woojeong.config.security.user.WjUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 *
 * @author vladimir.stankovic
 *
 * May 31, 2016
 */
@Component
public class JwtTokenFactory {
    private final JwtSettings settings;


    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Factory method for issuing new JWT Tokens.
     */
    public AccessJwtToken createAccessJwtToken(WjUserInfo userInfo) {

//        if (userInfo.getAuthorities() == null || userInfo.getAuthorities().isEmpty())
//            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims();
//        claims.setSubject(userInfo.getId());

//        if (userInfo.is_manager()) {
//            claims.put("spvs_idx", userInfo.getSpvs_idx());
//            claims.put("spvs_id", userInfo.getSpvs_id());
//            claims.put("spvs_name", userInfo.getSvps_name());
//            claims.put("spvs_office_code", userInfo.getSpvs_office_code());
//        } else {
//            claims.put("is_manager", userInfo.is_manager());
//            claims.put("is_manager", userInfo.is_manager());
//            claims.put("is_admin", userInfo.is_admin());
//            claims.put("user_idx", userInfo.getUser_idx());
//            claims.put("user_phone", userInfo.getUser_phone());
//            claims.put("user_id", userInfo.getUser_id());
//            claims.put("user_name", userInfo.getUser_name());
//            claims.put("user_address", userInfo.getUser_address());
//        }

//        claims.put("is_manager", userInfo.is_manager());
//        claims.put("with_master_key", userInfo.isWith_master_key());
//
        claims.put("user_idx", userInfo.getUser_idx());
        claims.put("user_phone", userInfo.getUser_phone());
        claims.put("user_id", userInfo.getUser_id());
        claims.put("user_name", userInfo.getUser_name());

        claims.put("scopes", userInfo.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();
        String token = Jwts.builder()
                        .setClaims(claims)
                        .setId(UUID.randomUUID().toString())
                        .setIssuer(settings.getTOKEN_ISSUER())
                        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                        .setExpiration(Date.from(currentTime
                                .plusMinutes(settings.getTOKEN_EXPIRATION_TIME())
                                .atZone(ZoneId.systemDefault()).toInstant()))
                        .signWith(SignatureAlgorithm.HS512, settings.getTOKEN_SIGNING_KEY())
                        .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(WjUserInfo chkUserInfo) {

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims();

        String token = Jwts.builder()
                          .setClaims(claims)
                          .setIssuer(settings.getTOKEN_ISSUER())
                          .setId(UUID.randomUUID().toString())
                          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                          .setExpiration(Date.from(currentTime
                              .plusMinutes(settings.getREFRESH_TOKEN_EXPIRATION_TIME())
                              .atZone(ZoneId.systemDefault()).toInstant()))
                          .signWith(SignatureAlgorithm.HS512, settings.getTOKEN_SIGNING_KEY())
                        .compact();

        return new AccessJwtToken(token, claims);

    }
}
