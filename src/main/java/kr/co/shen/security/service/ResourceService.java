package kr.co.shen.security.service;

import kr.co.shen.security.dto.Resource;
import kr.co.shen.security.repository.AuthRepository;
import kr.co.shen.security.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final AuthRepository authRepository;

    @Cacheable("allResources")
    public List<Resource> findAll() {
        List<Resource> resources = resourceRepository.findAll();
        resources.forEach(this::findAuthsByResource);
        return resources;
    }

    public void findAuthsByResource(Resource resource) {
        String uuid = resource.getUuid();
        Set<String> auths = authRepository.findByResource(uuid);
        resource.setAuths(auths);
    }
}
