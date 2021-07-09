package org.woojeong.config.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.woojeong.config.security.auth.ajax.AjaxLoginFailureHandler;
import org.woojeong.config.security.auth.ajax.AjaxLoginFilter;
import org.woojeong.config.security.auth.ajax.AjaxLoginProvider;
import org.woojeong.config.security.auth.ajax.AjaxLoginSuccessHandler;
import org.woojeong.config.security.auth.jwt.JwtAuthFilter;
import org.woojeong.config.security.auth.jwt.JwtAuthProvider;
import org.woojeong.config.security.auth.jwt.JwtSettings;
import org.woojeong.config.security.auth.jwt.JwtTokenFactory;
import org.woojeong.config.security.auth.jwt.extractor.JwtHeaderTokenExtractor;
import org.woojeong.config.security.auth.jwt.extractor.TokenExtractor;
import org.woojeong.config.security.etc.AjaxAccessDeniedHandler;
import org.woojeong.config.security.user.WjUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean(name="mainJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

    @Resource(name = "mainJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WjUserService.class);

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    protected static final String AUTHENTICATION_URL = "/auth/login";           //로그인 요청 url

    @Autowired
    ObjectMapper objectMapper;

    @Bean("tokenFactory")
    protected JwtTokenFactory tokenFactory() {
        return new JwtTokenFactory(jwtSettings);
    }
    @Autowired
    JwtSettings jwtSettings;
    @Autowired
    AuthManager authManager;

    @Autowired
    AjaxLoginFailureHandler ajaxLoginFailureHandler;

    @Bean public AuthManager authManager(){
        AuthManager authManager = new AuthManager(jdbcTemplate);
        return authManager;
    }

    @Bean
    protected JwtAuthProvider jwtAuthenticationProvider() {
        return new JwtAuthProvider(jwtSettings);
    }

    @Bean
    TokenExtractor tokenExtractor() {
        return new JwtHeaderTokenExtractor();
    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    protected AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AjaxLoginSuccessHandler(objectMapper, tokenFactory());
    }

    @Bean
    protected AjaxAccessDeniedHandler ajaxAccessDeniedHandler() {
        return new AjaxAccessDeniedHandler();
    }

    protected AjaxLoginFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginFilter filter = new AjaxLoginFilter(loginEntryPoint, authenticationSuccessHandler(), ajaxLoginFailureHandler, objectMapper);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
    protected JwtAuthFilter buildJwtTokenAuthenticationProcessingFilter(String pattern) throws Exception {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(pattern);
        JwtAuthFilter filter = new JwtAuthFilter(ajaxLoginFailureHandler, tokenExtractor(), authManager, matcher);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)    // 로그인의 경우

                .exceptionHandling()

                .accessDeniedHandler(ajaxAccessDeniedHandler())
                .and()
                .cors()
                .and()
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter("/api/v*/user/**"), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    protected AjaxLoginProvider ajaxAuthenticationProvider(){
        return new AjaxLoginProvider();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider());
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
//                "https://checking-frontend.ddhouse.co.kr",
//                "https://checking-frontend-test.ddhouse.co.kr",
//                "https://checking-frontend-quick-test.ddhouse.co.kr",
//                "https://drm.ddhouse.co.kr",
//                "https://office.ddhouse.co.kr",
//                "http://127.0.0.1:3000",
//                "http://127.0.0.1:8081",
//                "http://ssc.portfolio1000.com"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
