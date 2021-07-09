package org.woojeong.config.security.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSettings {

    //토큰 관련 설정
    @Value( "${token.expiration-time}")
    private Integer TOKEN_EXPIRATION_TIME;
    @Value( "${token.refresh-expiration-time}")
    private Integer REFRESH_TOKEN_EXPIRATION_TIME;
    @Value( "${token.issuer}")
    private String TOKEN_ISSUER;
    @Value( "${token.signing-key}")
    private String TOKEN_SIGNING_KEY;

    public Integer getTOKEN_EXPIRATION_TIME() {
        return TOKEN_EXPIRATION_TIME;
    }

    public Integer getREFRESH_TOKEN_EXPIRATION_TIME() {
        return REFRESH_TOKEN_EXPIRATION_TIME;
    }

    public String getTOKEN_ISSUER() {
        return TOKEN_ISSUER;
    }

    public String getTOKEN_SIGNING_KEY() {
        return TOKEN_SIGNING_KEY;
    }
}
