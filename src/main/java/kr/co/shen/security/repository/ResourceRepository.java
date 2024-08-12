package kr.co.shen.security.repository;

import kr.co.shen.security.dto.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceRepository {
    List<Resource> findAll();
}
