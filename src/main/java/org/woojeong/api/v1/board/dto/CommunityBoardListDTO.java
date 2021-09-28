package org.woojeong.api.v1.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityBoardListDTO {
    private String type;
    private Integer page;
    private Integer start;
    private Integer cnt;

    private Long my_user_idx;

    private String query;
}
