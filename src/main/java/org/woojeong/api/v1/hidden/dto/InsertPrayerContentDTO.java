package org.woojeong.api.v1.hidden.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertPrayerContentDTO {
    Long group_idx;
    Long prayer_idx;
    String content;
}
