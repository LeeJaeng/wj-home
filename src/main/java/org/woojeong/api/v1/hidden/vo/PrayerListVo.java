package org.woojeong.api.v1.hidden.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrayerListVo {
    Long group_idx;
    Long prayer_idx;
    String group_name;
    String prayer_name;
    String content;
}
