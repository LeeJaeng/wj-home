package org.woojeong.config.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.woojeong.config.security.auth.jwt.model.RawAccessJwtToken;
import org.woojeong.config.security.user.WjUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

@Autowired
    JwtSettings jwtSettings;

    @Autowired
    public JwtAuthProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        WjUserInfo userInfo;
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTOKEN_SIGNING_KEY());
        String subject = jwsClaims.getBody().getSubject();
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        userInfo = new WjUserInfo();

//        if (jwsClaims.getBody().get("is_manager", Boolean.class)) {
//            userInfo.setSpvs_idx(Long.valueOf(jwsClaims.getBody().get("spvs_idx", Integer.class)));
//            userInfo.setSpvs_id(jwsClaims.getBody().get("spvs_id", String.class));
//            userInfo.setSvps_name(jwsClaims.getBody().get("spvs_name", String.class));
//            userInfo.setSpvs_office_code(jwsClaims.getBody().get("spvs_office_code", Integer.class));
//        } else {
//            userInfo.set_manager(jwsClaims.getBody().get("is_manager", Boolean.class));
//            userInfo.set_supervisor(jwsClaims.getBody().get("is_manager", Boolean.class));
//            userInfo.set_admin(jwsClaims.getBody().get("is_admin", Boolean.class));
//            userInfo.setUser_idx(Long.valueOf(jwsClaims.getBody().get("user_idx", Integer.class)));
//            userInfo.setUser_phone(jwsClaims.getBody().get("user_phone", String.class));
//            userInfo.setUser_id(jwsClaims.getBody().get("user_id", String.class));
//            userInfo.setUser_name(jwsClaims.getBody().get("user_name", String.class));
//            userInfo.setUser_address(jwsClaims.getBody().get("user_address", String.class));
//        }

//        userInfo.set_manager(jwsClaims.getBody().get("is_manager", Boolean.class));
//        userInfo.set_admin(jwsClaims.getBody().get("is_admin", Boolean.class));
//        userInfo.setWith_master_key(jwsClaims.getBody().get("with_master_key", Boolean.class));
//        userInfo.setUser_idx(Long.valueOf(jwsClaims.getBody().get("user_idx", Integer.class)));
//        userInfo.setUser_phone(jwsClaims.getBody().get("user_phone", String.class));
//        userInfo.setUser_id(jwsClaims.getBody().get("user_id", String.class));
//        userInfo.setUser_name(jwsClaims.getBody().get("user_name", String.class));

        userInfo.setAuthorities(authorities);

        return new JwtAuthToken(userInfo, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthToken.class.isAssignableFrom(authentication));
    }
}
