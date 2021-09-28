package org.woojeong.api.v1.board.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CommunityBoardVo {
    private Long created_user_idx;
    private Long board_idx;
    private Integer category;
    private Boolean is_mine;
    private String title;
    private String content;
    private String date;
    private String thumb;
    private String created_date;
    private String updated_date;
    private List<Map<String, Object>> files;
    private Long file_cnt;
}
