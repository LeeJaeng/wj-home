package org.woojeong.api.v1.common;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.woojeong.util.S3Uploader;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommonDao dao;
    private final S3Uploader s3Uploader;

    // ===== 배너 =====

    @Cacheable("banners")
    public Map<String, Object> getMainBanners () {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", dao.getMainBanners());
        return retMap;
    }

    @Cacheable("histories")
    public Map<String, Object> getHistories () {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", dao.getHistories());
        return retMap;
    }

    @Cacheable("allBanners")
    public List<Map<String, Object>> getAllBanners () {
        return dao.getAllBanners();
    }

    @Caching(evict = {
        @CacheEvict("banners"),
        @CacheEvict("allBanners")
    })
    public boolean uploadBanner (MultipartFile file) throws Exception {
        String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstuvwxyz");
        String fileName = "images/banner/" + rand + "_" + file.getOriginalFilename();
        String url = s3Uploader.upload(file, fileName);

        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        dao.insertBanner(params);
        return true;
    }

    @Caching(evict = {
        @CacheEvict("banners"),
        @CacheEvict("allBanners")
    })
    public boolean updateBannerOrder (Long idx, Integer ord) {
        Map<String, Object> params = new HashMap<>();
        params.put("idx", idx);
        params.put("ord", ord);
        return dao.updateBannerOrder(params) > 0;
    }

    @Caching(evict = {
        @CacheEvict("banners"),
        @CacheEvict("allBanners")
    })
    public boolean toggleBannerValid (Long idx) {
        return dao.toggleBannerValid(idx) > 0;
    }

    @Caching(evict = {
        @CacheEvict("banners"),
        @CacheEvict("allBanners")
    })
    public boolean deleteBanner (Long idx) {
        return dao.deleteBanner(idx) > 0;
    }

    // ===== 섬기는 사람들 =====

    @Cacheable("staff")
    public Map<String, Object> getStaff () {
        List<Map<String, Object>> flat = dao.getStaff();
        List<Map<String, Object>> grouped = new ArrayList<>();
        Map<String, Map<String, Object>> categoryIndex = new LinkedHashMap<>();

        for (Map<String, Object> member : flat) {
            String category = (String) member.get("category");
            if (!categoryIndex.containsKey(category)) {
                Map<String, Object> group = new LinkedHashMap<>();
                group.put("category", category);
                group.put("category_ord", member.get("category_ord"));
                group.put("members", new ArrayList<Map<String, Object>>());
                categoryIndex.put(category, group);
                grouped.add(group);
            }
            ((List<Map<String, Object>>) categoryIndex.get(category).get("members")).add(member);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", grouped);
        return result;
    }

    @CacheEvict(value = "staff", allEntries = true)
    public boolean insertStaff (Map<String, Object> params, MultipartFile photo) throws Exception {
        if (photo != null && !photo.isEmpty()) {
            String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstuvwxyz");
            String fileName = "images/staff/" + rand + "_" + photo.getOriginalFilename();
            params.put("photo_url", s3Uploader.upload(photo, fileName));
        }
        return dao.insertStaff(params) > 0;
    }

    @CacheEvict(value = "staff", allEntries = true)
    public boolean updateStaff (Map<String, Object> params, MultipartFile photo) throws Exception {
        if (photo != null && !photo.isEmpty()) {
            String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstuvwxyz");
            String fileName = "images/staff/" + rand + "_" + photo.getOriginalFilename();
            params.put("photo_url", s3Uploader.upload(photo, fileName));
        }
        return dao.updateStaff(params) > 0;
    }

    @CacheEvict(value = "staff", allEntries = true)
    public boolean deleteStaff (Long idx) {
        return dao.deleteStaffSoft(idx) > 0;
    }

    @CacheEvict(value = "staff", allEntries = true)
    public void reorderStaff (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateStaffSortOrder(item);
        }
    }

    @CacheEvict(value = "staff", allEntries = true)
    public void reorderCategories (List<Map<String, Object>> categories) {
        for (Map<String, Object> cat : categories) {
            dao.updateCategoryOrd(cat);
            if (cat.containsKey("new_category") && !cat.get("new_category").equals(cat.get("category"))) {
                dao.renameCategory(cat);
            }
        }
    }

    // ===== 교육부서 =====

    @Cacheable("deptList")
    public Map<String, Object> getDepartmentList () {
        Map<String, Object> result = new HashMap<>();
        result.put("data", dao.getDepartmentList());
        return result;
    }

    @Cacheable(value = "deptDetail", key = "#deptKey")
    public Map<String, Object> getDepartmentDetail (String deptKey) {
        Map<String, Object> result = new HashMap<>();
        result.put("dept", dao.getDepartment(deptKey));
        result.put("sections", dao.getDepartmentSections(deptKey));
        result.put("staff", dao.getDepartmentStaff(deptKey));
        result.put("teachers", dao.getDepartmentTeachers(deptKey));
        result.put("images", dao.getDepartmentImages(deptKey));
        return result;
    }

    // 부서 기본정보 수정 (예배시간/장소)
    @CacheEvict(value = "deptDetail", key = "#params['dept_key']")
    public boolean updateDepartment (Map<String, Object> params) {
        return dao.updateDepartment(params) > 0;
    }

    // 부서 섹션
    @CacheEvict(value = "deptDetail", allEntries = true)
    public Map<String, Object> insertDepartmentSection (Map<String, Object> params) {
        dao.insertDepartmentSection(params);
        return params;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean updateDepartmentSection (Map<String, Object> params) {
        return dao.updateDepartmentSection(params) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean deleteDepartmentSection (Long idx) {
        return dao.deleteDepartmentSection(idx) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public void reorderDepartmentSections (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateDepartmentSectionSort(item);
        }
    }

    // 부서별 섬기는 사람
    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean insertDepartmentStaff (Map<String, Object> params) {
        return dao.insertDepartmentStaff(params) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean updateDepartmentStaff (Map<String, Object> params) {
        return dao.updateDepartmentStaff(params) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean deleteDepartmentStaff (Long idx) {
        return dao.deleteDepartmentStaff(idx) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public void reorderDepartmentStaff (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateDepartmentStaffSort(item);
        }
    }

    // 부서별 교사/목자
    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean insertDepartmentTeacher (Map<String, Object> params) {
        return dao.insertDepartmentTeacher(params) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean updateDepartmentTeacher (Map<String, Object> params) {
        return dao.updateDepartmentTeacher(params) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean deleteDepartmentTeacher (Long idx) {
        return dao.deleteDepartmentTeacher(idx) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public void reorderDepartmentTeachers (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateDepartmentTeacherSort(item);
        }
    }

    // 부서 이미지
    @CacheEvict(value = "deptDetail", allEntries = true)
    public Map<String, Object> uploadDepartmentImage (String deptKey, MultipartFile file, int sortOrder) throws Exception {
        String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstuvwxyz");
        String fileName = "images/department/" + rand + "_" + file.getOriginalFilename();
        String url = s3Uploader.upload(file, fileName);
        Map<String, Object> params = new HashMap<>();
        params.put("dept_key", deptKey);
        params.put("url", url);
        params.put("sort_order", sortOrder);
        dao.insertDepartmentImage(params);
        return params;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public boolean deleteDepartmentImage (Long idx) {
        return dao.deleteDepartmentImage(idx) > 0;
    }

    @CacheEvict(value = "deptDetail", allEntries = true)
    public void reorderDepartmentImages (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateDepartmentImageSort(item);
        }
    }

    // 부서 목록 관리 (생성/수정/삭제) → deptList + deptDetail 둘 다 초기화
    @Caching(evict = {
        @CacheEvict("deptList"),
        @CacheEvict(value = "deptDetail", allEntries = true)
    })
    public boolean insertDepartment (Map<String, Object> params) {
        return dao.insertDepartment(params) > 0;
    }

    @Caching(evict = {
        @CacheEvict("deptList"),
        @CacheEvict(value = "deptDetail", allEntries = true)
    })
    public void updateDepartmentMetas (List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            dao.updateDepartmentMeta(item);
        }
    }

    @Caching(evict = {
        @CacheEvict("deptList"),
        @CacheEvict(value = "deptDetail", allEntries = true)
    })
    public boolean deleteDepartmentSoft (String deptKey) {
        return dao.deleteDepartmentSoft(deptKey) > 0;
    }
}
