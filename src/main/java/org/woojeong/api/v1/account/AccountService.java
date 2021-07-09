package org.woojeong.api.v1.account;

//import kr.co.kface.checkingapiserver.api.entities.Sms;
//import kr.co.kface.checkingapiserver.api.entities.User;
//import kr.co.kface.checkingapiserver.api.v1.account.dto.RegisterDTO;
//import kr.co.kface.checkingapiserver.api.v1.sms.aligo.SmsMessengerAligo;
//import kr.co.kface.checkingapiserver.util.Util;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.woojeong.api.v1.account.dto.RegisterDTO;
import org.woojeong.config.security.user.WjUserVo;
import org.woojeong.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountDao accountDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    public boolean register (RegisterDTO registerDTO) {
//        이미 기존 전화번호가 있는 경우
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

//        if (accountRepo.findUserbyPhone(registerDTO.getPhone()).isPresent()) {
//            return false;
//        }
////        인증코드 확인 후 진행
//        if (accountRepo.findSmsWithCode(registerDTO.getPhone(), registerDTO.getCode()) > 0L) {
//            String salt = Util.createRandStr(20);
//            String hash = Util.md5Hash(registerDTO.getPassword() + salt);
//
//            User user = new User(
//                    registerDTO.getPhone(),
//                    registerDTO.getName(),
//                    salt, hash
//            );
//            return accountRepo.saveUser(user) > 0L;
//        }
        return true;
    }


//    public Map<String, Object> phoneVerify (String phone, boolean isForRegister) {
//
//        Map<String, Object> result = new HashMap<>();
//
//        if (
//                (isForRegister && accountDao.getUserByPhone(phone).size() > 0) ||
//                (!isForRegister && accountDao.getUserByPhone(phone).size() == 0)
//        ) {
//            result.put("statusCode", 2);
//            return result;
//        }
//
//        String code = "";
//        for (int i = 0; i < 6; i++) {
//            code += String.valueOf((int)(Math.random() * 10.0));
//        }
//
//        try {
//            Sms sms = new Sms(
//                    phone, code, "고객님의 체킹 가입 코드는 [" + code + "] 입니다."
//            );
//
//            SmsMessengerAligo aligo = new SmsMessengerAligo(accountRepo);
//
//            result.put("data", aligo.send(sms));
//            result.put("statusCode", 1);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put("statusCode", 0);
//            return result;
//        }
//    }





//    public boolean changePassword (Map<String, Object> params) {
////        가입되지 않은 전화번호인 경우
//        if (accountDao.getUserByPhone(params.get("phone").toString()).size() == 0) {
//            return false;
//        }
////        인증코드 확인 후 진행
//        if (accountDao.checkCode(params)) {
//            String salt = Util.createRandStr(20);
//            String hash = Util.md5Hash(params.get("password") + salt);
//            params.put("salt", salt);
//            params.put("hash", hash);
//
//            return accountDao.changePassword(params) > 0;
//        }
//        return false;
//    }

//    public List<Map<String, Object>> getUserByPhone (String userPhone) {
//        return accountDao.getUserByPhone(userPhone);
//    }

//    public List<User> findTestableUsers () {
//        return accountRepo.findTestableUsers();
//    }

//    public boolean checkUserPassword (Long userIdx, String password) {
//        Optional<User> userOpt = accountRepo.findUserbyIdx(userIdx);
//        if (userOpt.isPresent()) {
//            String salt = userOpt.map(User::getUser_salt).get();
//            String hash = userOpt.map(User::getUser_hash).get();
//
//            if (Util.md5Hash(password + salt).equals(hash)) { return true; }
//        }
//        return false;
//    }
}
