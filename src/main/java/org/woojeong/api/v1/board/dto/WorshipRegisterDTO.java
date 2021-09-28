package org.woojeong.api.v1.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class WorshipRegisterDTO {
    private Long user_idx;
    private Long board_idx;
    private Integer category;
    private String date;
    private String worship_type;
    private String host_name;
    private String title;
    private String verse;
    private String content;
}
