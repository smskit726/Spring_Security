package kr.co.shen.security.service;

import kr.co.shen.security.dto.User;
import kr.co.shen.security.repository.AuthRepository;
import kr.co.shen.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void post(User user) {
        String encPassword = passwordEncoder.encode(user.getUserPw());
        user.setUserPw(encPassword);

        userRepository.post(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if (user != null) {

            // auth uuid set
            Set<String> authId = userRepository.findAuthByUserId(username);

            // auth name set
            Set<String> auths = new HashSet<>();

            // 권한 찾아 넣어준다
            for( String id : authId ) {
                auths.add(authRepository.findById(id));
            }

            user.setAuths(auths);

            return user;
        }

        throw new UsernameNotFoundException(username);
    }
}
