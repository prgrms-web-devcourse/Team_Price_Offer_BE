package com.prgrms.offer.core.config;
import com.prgrms.offer.core.jwt.Jwt;
import com.prgrms.offer.core.jwt.JwtAuthenticationFilter;
import com.prgrms.offer.core.jwt.JwtAuthenticationProvider;
import com.prgrms.offer.core.jwt.JwtConfigure;
import com.prgrms.offer.core.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.prgrms.offer.core.oauth2.OAuth2AuthenticationSuccessHandler;
import com.prgrms.offer.domain.member.model.entity.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigure jwtConfigure;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    public WebSecurityConfig(JwtConfigure jwtConfigure, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.jwtConfigure = jwtConfigure;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
                jwtConfigure.getIssuer(),
                jwtConfigure.getClientSecret(),
                jwtConfigure.getExpirySeconds()
        );
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        Jwt jwt = getApplicationContext().getBean(Jwt.class);
        MemberService memberService = getApplicationContext().getBean(MemberService.class);
        return new JwtAuthenticationProvider(jwt, memberService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null;
            log.warn("{} is denied", principal, e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("ACCESS DENIED");
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/assets/**", "/h2-console/**");
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(Jwt jwt, MemberService memberService) {
        return new OAuth2AuthenticationSuccessHandler(jwt, memberService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/user/me").authenticated()
                .anyRequest().permitAll()
                .and()
                 // formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
                .formLogin().disable()
                .csrf().disable()
                .headers().disable()
                .httpBasic().disable()
                .rememberMe().disable()
                .logout().disable()
                 // Session 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // OAuth2 설정
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .successHandler(getApplicationContext().getBean(OAuth2AuthenticationSuccessHandler.class))
                .authorizedClientRepository(authorizedClientRepository)
                .and()
                 // 예외처리 핸들러
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                // 커스텀 JwtFilter 추가
                .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
        ;

    }

}