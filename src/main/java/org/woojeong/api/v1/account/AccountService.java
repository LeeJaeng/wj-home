package org.woojeong.api.v1.account;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.woojeong.api.v1.account.dto.RegisterDTO;
import org.woojeong.config.security.user.WjUserVo;
import org.woojeong.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountDao accountDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean register (RegisterDTO registerDTO) {
        if (accountDao.isExistId(registerDTO.getUser_id())) {
            return false;
        }

        String salt = Util.createRandStr(20);
        String hash = Util.md5Hash(registerDTO.getPassword() + salt);

        WjUserVo userVo = new WjUserVo();
        userVo.setUser_id(registerDTO.getUser_id());
        userVo.setUser_email(registerDTO.getUser_email());
        userVo.setUser_phone(registerDTO.getUser_phone());
        userVo.setUser_name(registerDTO.getUser_name());
        userVo.setUser_hash(hash);
        userVo.setUser_salt(salt);

        accountDao.insertUser(userVo);
        return true;
    }

    public boolean registerByAdmin (RegisterDTO registerDTO) {
        if (accountDao.isExistId(registerDTO.getUser_id())) {
            return false;
        }

        String salt = Util.createRandStr(20);
        String hash = Util.md5Hash(registerDTO.getPassword() + salt);

        WjUserVo userVo = new WjUserVo();
        userVo.setUser_id(registerDTO.getUser_id());
        userVo.setUser_email(registerDTO.getUser_email() != null ? registerDTO.getUser_email() : "");
        userVo.setUser_phone(registerDTO.getUser_phone() != null ? registerDTO.getUser_phone() : "");
        userVo.setUser_name(registerDTO.getUser_name());
        userVo.setUser_hash(hash);
        userVo.setUser_salt(salt);

        accountDao.insertUserWithAuth(userVo);

        Map<String, Object> authParams = new HashMap<>();
        authParams.put("user_idx", userVo.getUser_idx());
        authParams.put("auth", registerDTO.getAuth() != null ? registerDTO.getAuth() : 2);
        accountDao.insertUserAuth(authParams);

        return true;
    }

    public List<Map<String, Object>> getUserList () {
        return accountDao.getUserList();
    }

    public boolean updateUserAuth (Long userIdx, Integer auth) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_idx", userIdx);
        params.put("auth", auth);
        return accountDao.updateUserAuth(params) >= 0;
    }

    public boolean updateUserPassword (Long userIdx, String newPassword) {
        String salt = Util.createRandStr(20);
        String hash = Util.md5Hash(newPassword + salt);
        WjUserVo userVo = new WjUserVo();
        userVo.setUser_idx(userIdx);
        userVo.setUser_hash(hash);
        userVo.setUser_salt(salt);
        return accountDao.updateUserPassword(userVo) > 0;
    }

    public boolean deleteUser (Long userIdx) {
        return accountDao.deleteUser(userIdx) > 0;
    }
}
