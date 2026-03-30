package org.woojeong.api.v1.common;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.woojeong.config.security.user.WjUserInfo;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommonRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommonService commonService;

    @GetMapping(value = "/public/common/main-banners")
    public ResponseEntity getMainBanner () {
        return ResponseEntity.ok(commonService.getMainBanners());
    }

    @GetMapping(value = "/public/common/histories")
    public ResponseEntity getHistories () {
        return ResponseEntity.ok(commonService.getHistories());
    }

    // 슈퍼관리자: 전체 배너 목록 (비활성 포함)
    @GetMapping("/user/admin/banners")
    public ResponseEntity getAllBanners(@AuthenticationPrincipal WjUserInfo userInfo) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(commonService.getAllBanners());
    }

    // 슈퍼관리자: 배너 업로드
    @PostMapping("/user/admin/banners")
    public ResponseEntity uploadBanner(@AuthenticationPrincipal WjUserInfo userInfo,
                                       @RequestParam("file") MultipartFile file) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            commonService.uploadBanner(file);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error("배너 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 슈퍼관리자: 배너 순서 변경
    @PatchMapping("/user/admin/banners/{idx}/order")
    public ResponseEntity updateBannerOrder(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable Long idx,
                                            @RequestBody Map<String, Integer> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.updateBannerOrder(idx, body.get("ord"));
        return ResponseEntity.ok(true);
    }

    // 슈퍼관리자: 배너 활성/비활성 토글
    @PatchMapping("/user/admin/banners/{idx}/toggle")
    public ResponseEntity toggleBanner(@AuthenticationPrincipal WjUserInfo userInfo,
                                       @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.toggleBannerValid(idx);
        return ResponseEntity.ok(true);
    }

    // 슈퍼관리자: 배너 삭제
    @DeleteMapping("/user/admin/banners/{idx}")
    public ResponseEntity deleteBanner(@AuthenticationPrincipal WjUserInfo userInfo,
                                       @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteBanner(idx);
        return ResponseEntity.ok(true);
    }
}
