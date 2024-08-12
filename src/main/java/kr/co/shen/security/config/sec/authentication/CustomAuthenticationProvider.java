package kr.co.shen.security.config.sec.authentication;

import kr.co.shen.security.dto.User;
import kr.co.shen.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/*
* AuthenticationProvider 는 Authentication 객체에 내용을 채워서 보내준다
* UserDetailsService.loadByUsername() 통해 UserDetails 찾아 인증된 토큰 생성
* 실제 인증 로직을 처리하는 클래스
* */
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getPrincipal().toString();
        String userPw = authentication.getCredentials().toString();
        String encPassword = passwordEncoder.encode(userPw);

        User user = (User) userService.loadUserByUsername(userId);

        if ( user == null ) throw new UsernameNotFoundException(userId);

        if (!passwordEncoder.matches(encPassword, user.getUserPw()))  throw new BadCredentialsException(userId);

        return new UsernamePasswordAuthenticationToken(userId, encPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
