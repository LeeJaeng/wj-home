package org.woojeong.config.security.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.woojeong.api.v1.account.AccountDao;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WjUserService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountDao accountDao;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        WjUserInfo wjUserInfo = new WjUserInfo();
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        WjUserVo userVo = accountDao.getUserByUsername(userId);

        if (userVo == null) {
            throw new UsernameNotFoundException(userId);
        }


        wjUserInfo.setUser_idx(userVo.getUser_idx());
        wjUserInfo.setUser_phone(userVo.getUser_phone());
        wjUserInfo.setUser_name(userVo.getUser_name());
        wjUserInfo.setUser_salt(userVo.getUser_salt());
        wjUserInfo.setUser_hash(userVo.getUser_hash());
        authorities.add(new SimpleGrantedAuthority("USER"));
        Integer auth = accountDao.getUserAuth(userVo.getUser_idx());
        switch (auth) {
            case 1:
                wjUserInfo.setAdmin(true);
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                break;
            case 2:
                wjUserInfo.setManager(true);
                authorities.add(new SimpleGrantedAuthority("MANAGER"));
                break;
            case 3:
                wjUserInfo.setPraise(true);
                authorities.add(new SimpleGrantedAuthority("PRAISE"));
                break;
        }

        wjUserInfo.setAuthorities(authorities);
        return wjUserInfo;
    }

//    public String findMasterKey () {
//        return chkUserRepo.findMasterKey()
//                .map(MasterKey::getKey).orElse("");
//    }

}
