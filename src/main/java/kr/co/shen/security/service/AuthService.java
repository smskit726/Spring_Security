package kr.co.shen.security.service;


import kr.co.shen.security.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    public String findById(String uuid) {
        return authRepository.findById(uuid);
    }

    public List<String> findByAuth(String uuid) {
        return authRepository.findByAuth(uuid);
    }

    public Set<String> findByResource(String uuid){
        return authRepository.findByResource(uuid);
    }

    public void post(String auth) {
        String uuid = String.valueOf(UUID.randomUUID());
        authRepository.post(uuid, auth);
    }
}
