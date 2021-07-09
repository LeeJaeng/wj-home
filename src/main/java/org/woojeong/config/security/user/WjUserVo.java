package org.woojeong.config.security.user;

import lombok.*;

@Getter
@Setter
public class WjUserVo {
    private Long user_idx;
    private String user_name;
    private String user_id;
    private String user_hash;
    private String user_salt;
    private String user_email;
    private String user_phone;
    private String cdate;
}
