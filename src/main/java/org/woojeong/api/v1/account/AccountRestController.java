package org.woojeong.api.v1.account;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.woojeong.api.v1.account.dto.RegisterDTO;
import org.woojeong.config.security.user.WjUserInfo;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountService accountService;

    // 슈퍼관리자: 유저 목록 조회
    @GetMapping("/user/admin/users")
    public ResponseEntity getUsers(@AuthenticationPrincipal WjUserInfo userInfo) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(accountService.getUserList());
    }

    // 슈퍼관리자: 유저 등록
    @PostMapping("/user/admin/users")
    public ResponseEntity registerByAdmin(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @RequestBody RegisterDTO registerDTO) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (accountService.registerByAdmin(registerDTO)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 아이디입니다.");
    }

    // 슈퍼관리자: 권한 변경
    @PatchMapping("/user/admin/users/{userIdx}/auth")
    public ResponseEntity updateAuth(@AuthenticationPrincipal WjUserInfo userInfo,
                                     @PathVariable Long userIdx,
                                     @RequestBody Map<String, Integer> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        accountService.updateUserAuth(userIdx, body.get("auth"));
        return ResponseEntity.ok(true);
    }

    // 슈퍼관리자: 비밀번호 변경
    @PatchMapping("/user/admin/users/{userIdx}/password")
    public ResponseEntity updatePassword(@AuthenticationPrincipal WjUserInfo userInfo,
                                         @PathVariable Long userIdx,
                                         @RequestBody Map<String, String> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        accountService.updateUserPassword(userIdx, body.get("password"));
        return ResponseEntity.ok(true);
    }

    // 슈퍼관리자: 유저 삭제
    @DeleteMapping("/user/admin/users/{userIdx}")
    public ResponseEntity deleteUser(@AuthenticationPrincipal WjUserInfo userInfo,
                                     @PathVariable Long userIdx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        accountService.deleteUser(userIdx);
        return ResponseEntity.ok(true);
    }
}
