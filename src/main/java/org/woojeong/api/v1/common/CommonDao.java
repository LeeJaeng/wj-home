package org.woojeong.api.v1.common;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.woojeong.config.security.user.WjUserVo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommonDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public List<Map<String, Object>> getMainBanners () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getMainBanners");
    }

    // 간단해서 따로 DTO 안만듦
    public List<Map<String, Object>> getHistories () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getHistories");
    }
}
