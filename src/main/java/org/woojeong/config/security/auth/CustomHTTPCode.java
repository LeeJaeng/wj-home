package org.woojeong.config.security.auth;

import org.woojeong.util.EnumModel;

public enum CustomHTTPCode implements EnumModel {

    CUSTM_NOT_ALLOWED_IP("허용아이피아님", 901, true),
    CUSTM_FORCED_LOGOUT("강제로그아웃", 902, true),
    CUSTM_RE_LOGIN("재로그인요청", 903, true);


    private String title;       //코드명
    private Integer code;       //코드값
    private boolean valid;      //사용여부 (콤보박스 생성 시)

    CustomHTTPCode(String title, Integer code, boolean valid) {
        this.title = title;
        this.code = code;
        this.valid = valid;
    }


    @Override
    public String getKey() {
        return this.code + "";
    }

    @Override
    public String getValue() {
        return this.title;
    }

    @Override
    public boolean getValid() {
        return this.valid;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 코드값으로 찾기 - DB 저장값이 name()이랑 다른 경우
     */
    public static CustomHTTPCode getEnum(Integer code) {
        for (CustomHTTPCode v : values())
            if (v.getCode().equals(code)) return v;
        throw new IllegalArgumentException();
    }
}

