package org.woojeong.api.v1.board;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.woojeong.api.v1.board.dto.CommunityRegisterDTO;
import org.woojeong.api.v1.board.dto.WorshipBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.config.security.user.WjUserInfo;
import org.woojeong.config.security.user.WjUserVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardService boardService;


    @PostMapping(value = "/user/board/worship")
    public ResponseEntity worshipBoardRegister(@AuthenticationPrincipal WjUserInfo activeUser, @RequestBody WorshipRegisterDTO worshipRegisterDTO) {
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        worshipRegisterDTO.setUser_idx(activeUser.getUser_idx());
        if (boardService.register(worshipRegisterDTO)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping(value = "/user/board/worship")
    public ResponseEntity worshipBoardEdit(@AuthenticationPrincipal WjUserInfo activeUser, @RequestBody WorshipRegisterDTO worshipRegisterDTO) {
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        worshipRegisterDTO.setUser_idx(activeUser.getUser_idx());
        if (boardService.editWorshipBoard(worshipRegisterDTO)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(value = "/user/board/worship/{idx}")
    public ResponseEntity worshipBoardDelete(@AuthenticationPrincipal WjUserInfo activeUser, @PathVariable(name = "idx") Long idx) {
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (boardService.deleteWorshipBoard(idx)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping(value = "/user/board/worship")
    public ResponseEntity getWorshipBoardList(@AuthenticationPrincipal WjUserInfo activeUser,
                                              @RequestParam(name = "type") String type,
                                              @RequestParam(name = "page") Integer page,
                                              @RequestParam(name = "init") Boolean init
    ) {
        return ResponseEntity.ok(boardService.getWorshipBoardList(activeUser, type, page, init));
    }


    @GetMapping(value = "/user/board/worship/{idx}")
    public ResponseEntity getWorshipBoard(@AuthenticationPrincipal WjUserInfo activeUser,
                                            @PathVariable(name = "idx") Long idx
    ) {
        return ResponseEntity.ok(boardService.getWorshipBoard(activeUser.getUser_idx(), idx));
    }


    @PostMapping(value = "/user/board/community", produces = "application/json; charset=utf-8")
    public ResponseEntity communityBoardRegister(@AuthenticationPrincipal WjUserInfo activeUser,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                 @RequestParam(name="category") Integer category,
                                                 @RequestParam(name="title", defaultValue = "") String title,
                                                 @RequestParam(name="content", defaultValue = "") String content){
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("title", title);
        params.put("content", content);
        params.put("user_idx", activeUser.getUser_idx());

        if (boardService.registerCommunityBoard(files, params)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/user/board/community/edit", produces = "application/json; charset=utf-8")
    public ResponseEntity communityBoardEdit(@AuthenticationPrincipal WjUserInfo activeUser,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                 @RequestParam(name="board_idx") Integer board_idx,
                                                 @RequestParam(name="category") Integer category,
                                                 @RequestParam(name="title", defaultValue = "") String title,
                                                 @RequestParam(name="deleted_files", defaultValue = "") String deletedFiles,
                                                 @RequestParam(name="content", defaultValue = "") String content){
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Map<String, Object> params = new HashMap<>();
        params.put("board_idx", board_idx);
        params.put("category", category);
        params.put("title", title);
        params.put("content", content);
        params.put("user_idx", activeUser.getUser_idx());
        if (!deletedFiles.equals("")) {
            params.put("deleted_files", deletedFiles.split(","));
        }

        if (boardService.editCommunityBoard(files, params)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }




    @GetMapping(value = "/user/board/community")
    public ResponseEntity getCommunityBoardList(@AuthenticationPrincipal WjUserInfo activeUser,
                                              @RequestParam(name = "type") String type,
                                              @RequestParam(name = "page") Integer page,
                                              @RequestParam(name = "init") Boolean init
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoardList(activeUser, type, page, init));
    }



    @GetMapping(value = "/user/board/community/{idx}")
    public ResponseEntity getCommunityBoard(@AuthenticationPrincipal WjUserInfo activeUser,
                                                @PathVariable(name = "idx") Long idx
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoard(activeUser.getUser_idx(), idx));
    }

    @DeleteMapping(value = "/user/board/community/{idx}")
    public ResponseEntity communityBoardDelete(@AuthenticationPrincipal WjUserInfo activeUser, @PathVariable(name = "idx") Long idx) {
        if (!activeUser.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (boardService.deleteCommunityBoard(idx)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
