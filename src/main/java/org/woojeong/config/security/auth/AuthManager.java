package org.woojeong.config.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<Long, String> users;
    private final JdbcTemplate jdbcTemplate;

    public AuthManager(JdbcTemplate jdbcTemplate){
        this.users = new HashMap<>();
        this.jdbcTemplate = jdbcTemplate;
    }


    public void addUser(Long user_idx, String msg){
        // TODO :
        // local -> dru 로 처리는 되나 drm->dru 로는 apache에서 403에러를 뿌린다.. --> 해결못함...
        // 일단
        //     1. drm에서 adduser하는 경우 DB에 저장하고,
        //     2. dru에서 DB에서 확인하는 걸로 하자...
        if(this.users.containsKey(user_idx))
            this.users.remove(user_idx);
        this.users.put(user_idx, msg);
        logger.info("관리대상 아이디를 추가한다 user_idx:{} ", user_idx);
    }

    public boolean hasUser(Long user_idx){
        logger.info("관리대상의 아이디인지 확인한다 user_idx:{} ", user_idx);

        //TODO :
        /*if(this.users.containsKey(user_idx)){
            return true;
        }*/
//        int cnt = jdbcTemplate.queryForObject(" select count(*) from aa_dd_user_logout where user_idx = ? ", new Object[]{user_idx}, Integer.class);
//        if(cnt>0){
//            jdbcTemplate.update(" delete from aa_dd_user_logout where user_idx = ? ", new Object[]{user_idx});
//            return true;
//        }

        return false;
    }

    public void removeUser(Long user_idx){
        this.users.remove(user_idx);
    }

    public String getCode(Long user_idx){
        // TODO :
        return "logout";
        //return users.get(user_idx);
    }
}
