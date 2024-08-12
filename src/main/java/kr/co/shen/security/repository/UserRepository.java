package kr.co.shen.security.repository;

import kr.co.shen.security.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface UserRepository {

    User findByUserId(String userId);
    Set<String> findAuthByUserId(String userId);
    void post(User user);

}
