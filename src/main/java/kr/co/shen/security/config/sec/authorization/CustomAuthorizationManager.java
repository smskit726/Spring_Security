package kr.co.shen.security.config.sec.authorization;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.shen.security.dto.Resource;
import kr.co.shen.security.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;

import java.util.List;
import java.util.function.Supplier;


/*
* 최종 접근 제어 결정을 내리는 역할
* 권한 체크를 담당하는 녀석
* */
@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    // TODO (역할과 권한)

    private final ResourceService resourceService;

    private final AuthorizationManager<RequestAuthorizationContext> permitAll =
            (authentication, object) -> new AuthorizationDecision(true);

    public CustomAuthorizationManager(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        // resource 조회
        List<Resource> resources = resourceService.findAll();

        // 체크 위임할 매니저 생성
        RequestMatcherDelegatingAuthorizationManager delegate = buildAuthorizationManager(resources);
        return delegate.check(authentication, request);
    }

    private RequestMatcherDelegatingAuthorizationManager buildAuthorizationManager(List<Resource> resources) {
        RequestMatcherDelegatingAuthorizationManager.Builder builder = RequestMatcherDelegatingAuthorizationManager.builder();

        resources.stream()
                .filter(Resource::hasUrl)
                .forEach(resource -> builder.add(createRequestMatcher(resource), createAuthorizationManager(resource)));

        builder.add(new DispatcherTypeRequestMatcher(DispatcherType.ERROR), permitAll);
        builder.add(new DispatcherTypeRequestMatcher(DispatcherType.FORWARD), permitAll);

        return builder.build();
    }

    private AntPathRequestMatcher createRequestMatcher(Resource resource) {
        return new AntPathRequestMatcher(resource.getUrl(), resource.getMethod());
    }

    private AuthorizationManager<RequestAuthorizationContext> createAuthorizationManager(Resource resource) {
        if(resource.hasAuths()){
            String[] authorities = resource.getAuths().toArray(new String[0]);
            return AuthorityAuthorizationManager.hasAnyAuthority(authorities);
        }

        // T_RESOURCE_AUTH 테이블에 허용권한이 없다면 모두 접근 가능
        return permitAll;
    }
}