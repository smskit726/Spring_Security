package kr.co.shen.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Alias("resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private String uuid;
    private String url;
    private String method;
    private Set<String> auths;


    public boolean hasUrl() {
        return StringUtils.isNotEmpty(this.url);
    }

    public boolean hasAuths() {
        return !CollectionUtils.isEmpty(auths);
    }

}
