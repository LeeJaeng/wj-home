package org.woojeong.api.v1.account.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class RegisterDTO {
    @NonNull
    private String user_id;
    @NonNull
    private String password;
    private String user_name;
    private String user_email;
    private String user_phone;
}
