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

import java.util.HashMap;
import java.util.List;
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

    // ===== 섬기는 사람들 =====
    @GetMapping("/public/staff")
    public ResponseEntity getStaff() {
        return ResponseEntity.ok(commonService.getStaff());
    }

    @PostMapping("/user/admin/staff")
    public ResponseEntity insertStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                      @RequestParam String category,
                                      @RequestParam(defaultValue = "0") int category_ord,
                                      @RequestParam String name,
                                      @RequestParam(required = false) String job,
                                      @RequestParam(required = false) String phone,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(defaultValue = "0") int sort_order,
                                      @RequestParam(required = false) MultipartFile photo) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("category", category);
            params.put("category_ord", category_ord);
            params.put("name", name);
            params.put("job", job);
            params.put("phone", phone);
            params.put("email", email);
            params.put("sort_order", sort_order);
            commonService.insertStaff(params, photo);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error("스태프 등록 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/user/admin/staff/{idx}")
    public ResponseEntity updateStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                      @PathVariable Long idx,
                                      @RequestParam String category,
                                      @RequestParam(defaultValue = "0") int category_ord,
                                      @RequestParam String name,
                                      @RequestParam(required = false) String job,
                                      @RequestParam(required = false) String phone,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(defaultValue = "0") int sort_order,
                                      @RequestParam(required = false) MultipartFile photo) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idx", idx);
            params.put("category", category);
            params.put("category_ord", category_ord);
            params.put("name", name);
            params.put("job", job);
            params.put("phone", phone);
            params.put("email", email);
            params.put("sort_order", sort_order);
            commonService.updateStaff(params, photo);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error("스태프 수정 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/user/admin/staff/{idx}")
    public ResponseEntity deleteStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                      @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteStaff(idx);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/staff/sort")
    public ResponseEntity reorderStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                       @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderStaff(items);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/staff/categories")
    public ResponseEntity reorderCategories(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @RequestBody List<Map<String, Object>> categories) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderCategories(categories);
        return ResponseEntity.ok(true);
    }

    // ===== 교육부서 =====
    @GetMapping("/public/department")
    public ResponseEntity getDepartmentList() {
        return ResponseEntity.ok(commonService.getDepartmentList());
    }

    @GetMapping("/public/department/{deptKey}")
    public ResponseEntity getDepartmentDetail(@PathVariable String deptKey) {
        return ResponseEntity.ok(commonService.getDepartmentDetail(deptKey));
    }

    @PutMapping("/user/admin/department/{deptKey}")
    public ResponseEntity updateDepartment(@AuthenticationPrincipal WjUserInfo userInfo,
                                           @PathVariable String deptKey,
                                           @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("dept_key", deptKey);
        commonService.updateDepartment(body);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/user/admin/department/{deptKey}/staff")
    public ResponseEntity insertDeptStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @PathVariable String deptKey,
                                          @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("dept_key", deptKey);
        commonService.insertDepartmentStaff(body);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/staff/{idx}")
    public ResponseEntity updateDeptStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @PathVariable String deptKey,
                                          @PathVariable Long idx,
                                          @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("idx", idx);
        commonService.updateDepartmentStaff(body);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/user/admin/department/{deptKey}/staff/{idx}")
    public ResponseEntity deleteDeptStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @PathVariable String deptKey,
                                          @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteDepartmentStaff(idx);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/user/admin/department/{deptKey}/teacher")
    public ResponseEntity insertDeptTeacher(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("dept_key", deptKey);
        commonService.insertDepartmentTeacher(body);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/teacher/{idx}")
    public ResponseEntity updateDeptTeacher(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @PathVariable Long idx,
                                            @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("idx", idx);
        commonService.updateDepartmentTeacher(body);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/user/admin/department/{deptKey}/teacher/{idx}")
    public ResponseEntity deleteDeptTeacher(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteDepartmentTeacher(idx);
        return ResponseEntity.ok(true);
    }

    // ===== 부서 섹션 =====
    @PostMapping("/user/admin/department/{deptKey}/sections")
    public ResponseEntity insertDeptSection(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("dept_key", deptKey);
        return ResponseEntity.ok(commonService.insertDepartmentSection(body));
    }

    @PutMapping("/user/admin/department/{deptKey}/sections/{idx}")
    public ResponseEntity updateDeptSection(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @PathVariable Long idx,
                                            @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        body.put("idx", idx);
        commonService.updateDepartmentSection(body);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/user/admin/department/{deptKey}/sections/{idx}")
    public ResponseEntity deleteDeptSection(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteDepartmentSection(idx);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/sections/sort")
    public ResponseEntity reorderDeptSections(@AuthenticationPrincipal WjUserInfo userInfo,
                                              @PathVariable String deptKey,
                                              @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderDepartmentSections(items);
        return ResponseEntity.ok(true);
    }

    // ===== 부서 등록/수정/삭제/순서 =====
    @PostMapping("/user/admin/department")
    public ResponseEntity insertDepartment(@AuthenticationPrincipal WjUserInfo userInfo,
                                           @RequestBody Map<String, Object> body) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.insertDepartment(body);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/sort")
    public ResponseEntity updateDepartmentMetas(@AuthenticationPrincipal WjUserInfo userInfo,
                                                @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.updateDepartmentMetas(items);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/user/admin/department/{deptKey}")
    public ResponseEntity deleteDepartment(@AuthenticationPrincipal WjUserInfo userInfo,
                                           @PathVariable String deptKey) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteDepartmentSoft(deptKey);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/staff/sort")
    public ResponseEntity reorderDeptStaff(@AuthenticationPrincipal WjUserInfo userInfo,
                                           @PathVariable String deptKey,
                                           @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderDepartmentStaff(items);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/teacher/sort")
    public ResponseEntity reorderDeptTeachers(@AuthenticationPrincipal WjUserInfo userInfo,
                                              @PathVariable String deptKey,
                                              @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderDepartmentTeachers(items);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/user/admin/department/{deptKey}/images")
    public ResponseEntity uploadDeptImage(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @PathVariable String deptKey,
                                          @RequestParam("file") MultipartFile file,
                                          @RequestParam(defaultValue = "0") int sort_order) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            Map<String, Object> result = commonService.uploadDepartmentImage(deptKey, file, sort_order);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("부서 이미지 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/user/admin/department/{deptKey}/images/{idx}")
    public ResponseEntity deleteDeptImage(@AuthenticationPrincipal WjUserInfo userInfo,
                                          @PathVariable String deptKey,
                                          @PathVariable Long idx) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.deleteDepartmentImage(idx);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/user/admin/department/{deptKey}/images/sort")
    public ResponseEntity reorderDeptImages(@AuthenticationPrincipal WjUserInfo userInfo,
                                            @PathVariable String deptKey,
                                            @RequestBody List<Map<String, Object>> items) {
        if (!userInfo.isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        commonService.reorderDepartmentImages(items);
        return ResponseEntity.ok(true);
    }
}
