package org.woojeong.api.v1.board.vo;

import lombok.*;

@Getter
@Setter
public class WorshipBoardListPaging {
    private Long totalCnt;
    private Integer curPage;
    private Integer pageCnt = 10;
    private Integer cntPerPage;
}
