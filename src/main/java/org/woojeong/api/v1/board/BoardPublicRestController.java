package org.woojeong.api.v1.board;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 비로그인 사용자용 게시판 공개 API
 * GET 조회만 허용 (등록/수정/삭제는 /api/v1/user/board/** 유지)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/board")
public class BoardPublicRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardService boardService;

    // =============================
    // 설교 게시판 (공개 조회)
    // =============================

    @GetMapping("/worship")
    public ResponseEntity getWorshipBoardList(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "host", required = false) String host,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "init") Boolean init
    ) {
        return ResponseEntity.ok(boardService.getWorshipBoardListPublic(type, host, page, init));
    }

    @GetMapping("/worship/preachers")
    public ResponseEntity getWorshipPreachers() {
        return ResponseEntity.ok(boardService.getWorshipPreachersPublic());
    }

    @GetMapping("/worship/{idx}")
    public ResponseEntity getWorshipBoard(@PathVariable(name = "idx") Long idx) {
        return ResponseEntity.ok(boardService.getWorshipBoardPublic(idx));
    }

    // =============================
    // 커뮤니티 게시판 (공개 조회)
    // =============================

    @GetMapping("/community")
    public ResponseEntity getCommunityBoardList(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "init") Boolean init
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoardListPublic(type, page, init));
    }

    @GetMapping("/community/{idx}")
    public ResponseEntity getCommunityBoard(@PathVariable(name = "idx") Long idx) {
        return ResponseEntity.ok(boardService.getCommunityBoardPublic(idx));
    }
}
