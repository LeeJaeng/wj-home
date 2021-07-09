package org.woojeong.config.security.auth.jwt.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
public class JwtHeaderTokenExtractor implements TokenExtractor {
    public static String HEADER_PREFIX = "Bearer ";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String extract(String header) {
        logger.debug("token header is {}", header);

        if (header == null || header.length() == 0) {
            throw new AuthenticationServiceException("인증헤더값이 없습니다. [header="+header+"]");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("잘못된 인증헤더값 입니다. [header="+header+"]");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
