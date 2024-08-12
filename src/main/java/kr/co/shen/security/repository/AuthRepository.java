package kr.co.shen.security.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRepository {

    String findById(String uuid);
    List<String> findByAuth(String uuid);
    Set<String> findByResource(String uuid);
    void post(String uuid, String auth);
}
