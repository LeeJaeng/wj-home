package org.woojeong.config.security.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WjUserInfo implements UserDetails {

    private List<GrantedAuthority> authorities;

    private Long user_idx;
    private String user_name;
    private String user_id;
    private String user_hash;
    private String user_salt;
    private String user_email;
    private String user_phone;

    private boolean isAdmin = false;
    private boolean isManager = false;
    private boolean isPraise = false;

    public String getId() {
        return user_id;
    }
    public Long getUserIdx() {
        return user_idx;
    }
    public String getUserPhone() {
        return user_phone;
    }
    public String getUserEmail() {
        return user_email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
