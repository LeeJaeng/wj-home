package org.woojeong.config.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.woojeong.config.security.auth.jwt.exception.AuthMethodNotSupportedException;
import org.woojeong.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AjaxLoginFilter
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
public class AjaxLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    @Autowired
    public AjaxLoginFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                           AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
        super(defaultProcessUrl);



        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

//        if (!"POST".equals(request.getMethod()) || !WebUtil.isAjax(request)) {
        if (!WebUtil.isAjax(request)) {
            if(logger.isDebugEnabled()) {
                logger.debug("Authentication method not supported. Request method: " + request.getMethod());
            }
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        AjaxLoginRequest ajaxLoginRequest = objectMapper.readValue(request.getInputStream(), AjaxLoginRequest.class);

        //단순히 아아디/패스워드를 담는 토큰에 넣어서 전달
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                ajaxLoginRequest.getUser_id(), ajaxLoginRequest.getPassword()
        );
        return this.getAuthenticationManager().authenticate(token);
    }


    /**
     *  AjaxAuthenticationProvider -> 성공 (Success Handler 위임)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    /**
     *  AjaxAuthenticationProvider -> 실패 (Failure Handler 위임)
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();   //호출하는 이유는 https://okky.kr/article/258061 여기 한번 볼 것.
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
