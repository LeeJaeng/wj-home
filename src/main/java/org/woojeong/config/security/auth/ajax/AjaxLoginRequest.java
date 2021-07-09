package org.woojeong.config.security.auth.ajax;

import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class AjaxLoginRequest {
    private @NonNull String user_id;
    private @NonNull String password;
}
