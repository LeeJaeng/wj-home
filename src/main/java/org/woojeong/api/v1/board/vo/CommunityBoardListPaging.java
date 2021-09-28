package org.woojeong.api.v1.board.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityBoardListPaging {
    private Long totalCnt;
    private Integer curPage;
    private Integer pageCnt = 10;
    private Integer cntPerPage;
}
