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

    public List<Map<String, Object>> getHistories () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getHistories");
    }

    public List<Map<String, Object>> getAllBanners () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getAllBanners");
    }

    public int insertBanner (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertBanner", params);
    }

    public int updateBannerOrder (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateBannerOrder", params);
    }

    public int toggleBannerValid (Long idx) {
        return sqlSessionTemplate.update("org.mybatis.example.common.toggleBannerValid", idx);
    }

    public int deleteBanner (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteBanner", idx);
    }
}
