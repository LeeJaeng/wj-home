package org.woojeong.config.security.auth.jwt;

import org.woojeong.config.security.auth.AuthManager;
import org.woojeong.config.security.auth.SecurityConfiguration;
import org.woojeong.config.security.auth.jwt.extractor.TokenExtractor;
import org.woojeong.config.security.auth.jwt.model.RawAccessJwtToken;
import org.woojeong.config.security.user.WjUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs validation of provided JWT Token.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;
    private final AuthManager authManager;

    @Autowired
    public JwtAuthFilter(AuthenticationFailureHandler failureHandler,
                         TokenExtractor tokenExtractor, AuthManager authManager, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
        this.authManager = authManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {


        String tokenPayload = request.getHeader(SecurityConfiguration.AUTHENTICATION_HEADER_NAME);

        //토큰이 없는경우
        if(tokenPayload == null || (tokenPayload!=null && "".equals(tokenPayload))){
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));

            WjUserInfo guest = new WjUserInfo();
//            guest.setUser_idx(-1L);
//            guest.setAuthorities(authorities);

            return new AnonymousAuthenticationToken("key", guest,  authorities);
        }

        RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
        return getAuthenticationManager().authenticate(new JwtAuthToken(token));
    }

    /**
     *  JwtAuthenticationProvider -> 성공
     *      - SecurityContext 에 등록
     *      - filter chain 호출
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        //강제로그아웃 등, 사용자권한 변경에 따른 액션 처리
        WjUserInfo userInfo = (WjUserInfo)context.getAuthentication().getPrincipal();
//        if(authManager.hasUser(userInfo.getUser_idx())){
//            String code = authManager.getCode(userInfo.getUser_idx());
//
//            if("logout".equals(code)){
//                authManager.removeUser(userInfo.getUser_idx());
//                response.setContentType("application/json");
//                response.setCharacterEncoding("utf-8");
//                response.setStatus(CustomHTTPCode.CUSTM_FORCED_LOGOUT.getCode());    //custom status 코드
//
//                /*PrintWriter out = response.getWriter();
//                JSONObject jsonObject = new JSONObject();
//                out.print(jsonObject);
//                out.flush();*/
//                return;
//            }
//        }


        chain.doFilter(request, response);
    }

    /**
     *  JwtAuthenticationProvider -> 실패 (Failure Handler 위임)
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
