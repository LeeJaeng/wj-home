package org.woojeong.api.v1.account;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.woojeong.config.security.user.WjUserVo;

@Repository
@RequiredArgsConstructor
public class AccountDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public WjUserVo getUserByUsername (String userId) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.account.getUserByUsername", userId);
    }
    public boolean isExistId (String userId) {
        return (Long)sqlSessionTemplate.selectOne("org.mybatis.example.account.isExistId", userId) > 0;
    }

    public int insertUser (WjUserVo userVo) {
        return sqlSessionTemplate.insert("org.mybatis.example.account.insertUser", userVo);
    }

    public Integer getUserAuth (Long userIdx) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.account.getUserAuth", userIdx);
    }


}
