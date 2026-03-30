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

    public int insertUserWithAuth (WjUserVo userVo) {
        return sqlSessionTemplate.insert("org.mybatis.example.account.insertUserWithAuth", userVo);
    }

    public int insertUserAuth (java.util.Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.account.insertUserAuth", params);
    }

    public java.util.List<java.util.Map<String, Object>> getUserList () {
        return sqlSessionTemplate.selectList("org.mybatis.example.account.getUserList");
    }

    public int updateUserAuth (java.util.Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.account.updateUserAuth", params);
    }

    public int updateUserPassword (WjUserVo userVo) {
        return sqlSessionTemplate.update("org.mybatis.example.account.updateUserPassword", userVo);
    }

    public int deleteUser (Long userIdx) {
        return sqlSessionTemplate.delete("org.mybatis.example.account.deleteUser", userIdx);
    }
}
