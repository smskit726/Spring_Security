package kr.co.shen.security.config.sec;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.shen.security.config.sec.authentication.CustomAuthenticationFailureHandler;
import kr.co.shen.security.config.sec.authentication.CustomAuthenticationProvider;
import kr.co.shen.security.config.sec.authentication.CustomAuthenticationSuccessHandler;
import kr.co.shen.security.config.sec.authorization.CustomAuthorizationFilter;
import kr.co.shen.security.config.sec.authorization.CustomAuthorizationManager;
import kr.co.shen.security.service.ResourceService;
import kr.co.shen.security.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/*
* Request -> Authentication (Filter, Manager, Provider)  -> AuthorizationFilter (Filter, Manager)
*
* Authentication (로그인)
* Filter : 인증요청 가로채서 Authentication 객체 만들고 Manager 한테 줘서 인증하라고 시킴
* Manager : 실제 인증을 하는 Provider 관리, Authentication 객체 받으면 Provider 한테 인증하라고 시킴
* Provider : 인증
*
* Authorization (권한) : Authentication 안에 Authorities 가지고 접근을 제어
* Filter : URL 통해 사용자의 리소스에 대한 액세스를 제한하는 권한 직접 부여 or Manager 한테 체크하라고 시킴
* Manager : 권한 체크
*
* @EnableWebSecurity(debug = true) 로 debugging, 필터 확인 가능
* */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    protected final String JSESSIONID = "JSESSIONID";

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final UserService userService;
    private final ResourceService resourceService;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider, UserService userService, ResourceService resourceService) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.userService = userService;
        this.resourceService = resourceService;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /*
    * Spring Boot는 Bean들 중에 Filter가 있으면 자동으로 Filter Chain에 등록됨
    * AuthorizationFilter 에서 OncePerRequestFilter를 상속받도록 변경
    * */
    @Bean
    public OncePerRequestFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter(authorizationManager());
    }

    @Bean
    public AuthorizationManager<HttpServletRequest> authorizationManager() {
        return new CustomAuthorizationManager(resourceService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        /*
        * 1. addFilterBefore() : 기본 Filter 이전 필터 추가 >> 지속 적용
        *    db 조회 후 메뉴별 접근 허용 권한 체크 >> 동적으로 변경이 가능
        * 2. authorizeHttpRequest() : 기본 권한 체크 설정, 배포시 적용 (변경 안됨)
        *    필터 통과 후 작동, 필터에서 권한 체크를 하므로 모두 허용으로 설정하였음.
        * */

        http
                .addFilterBefore(customAuthorizationFilter(), AuthorizationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        http
                .userDetailsService(userService)
                .authenticationProvider(customAuthenticationProvider);

        http
                .formLogin(config ->
                        config.loginPage("/login")
                                .usernameParameter("userId")
                                .passwordParameter("userPw")
                                .loginProcessingUrl("/loginProc")
                                .successHandler(customAuthenticationSuccessHandler())      // 성공하면
                                .failureHandler(customAuthenticationFailureHandler())      // 실패하면
                                .permitAll())
                .logout(config ->
                        config.deleteCookies(this.JSESSIONID)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll());

        return http.build();
    }
}