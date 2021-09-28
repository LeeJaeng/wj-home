package org.woojeong.api.v1.board.vo;

import lombok.*;

@Getter
@Setter
public class WorshipBoardVo {
    private Long created_user_idx;
    private Long board_idx;
    private Integer category;
    private String date;
    private String worship_type;
    private String host_name;
    private String title;
    private String verse;
    private String content;
    private String created_date;
    private String updated_date;
    private Boolean is_mine;
}
