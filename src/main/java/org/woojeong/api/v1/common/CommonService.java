package org.woojeong.api.v1.common;

//import kr.co.kface.checkingapiserver.api.entities.Sms;
//import kr.co.kface.checkingapiserver.api.entities.User;
//import kr.co.kface.checkingapiserver.api.v1.account.dto.RegisterDTO;
//import kr.co.kface.checkingapiserver.api.v1.sms.aligo.SmsMessengerAligo;
//import kr.co.kface.checkingapiserver.util.Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.woojeong.util.S3Uploader;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommonDao dao;
    private final S3Uploader s3Uploader;

    public Map<String, Object> getMainBanners () {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", dao.getMainBanners());
        return retMap;
    }

    public Map<String, Object> getHistories () {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", dao.getHistories());
        return retMap;
    }

    public List<Map<String, Object>> getAllBanners () {
        return dao.getAllBanners();
    }

    public boolean uploadBanner (MultipartFile file) throws Exception {
        String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstuvwxyz");
        String fileName = "images/banner/" + rand + "_" + file.getOriginalFilename();
        String url = s3Uploader.upload(file, fileName);

        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        dao.insertBanner(params);
        return true;
    }

    public boolean updateBannerOrder (Long idx, Integer ord) {
        Map<String, Object> params = new HashMap<>();
        params.put("idx", idx);
        params.put("ord", ord);
        return dao.updateBannerOrder(params) > 0;
    }

    public boolean toggleBannerValid (Long idx) {
        return dao.toggleBannerValid(idx) > 0;
    }

    public boolean deleteBanner (Long idx) {
        return dao.deleteBanner(idx) > 0;
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
