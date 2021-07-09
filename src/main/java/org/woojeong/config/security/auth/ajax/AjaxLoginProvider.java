package org.woojeong.config.security.auth.ajax;

import org.springframework.security.authentication.BadCredentialsException;
import org.woojeong.config.security.user.WjUserInfo;
import org.woojeong.config.security.user.WjUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.woojeong.util.Util;

import java.util.Collection;

@Component
public class AjaxLoginProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WjUserService wjUserService;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        logger.debug(authentication.toString());

        String id = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        WjUserInfo userInfo
                = (WjUserInfo) wjUserService.loadUserByUsername(id);
        Collection<? extends GrantedAuthority> authorities;

        if ( !Util.md5Hash(pwd + userInfo.getUser_salt())
                                .equals(userInfo.getUser_hash())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }


        authorities = userInfo.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userInfo, pwd, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
