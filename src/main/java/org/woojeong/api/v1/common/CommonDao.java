package org.woojeong.api.v1.common;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.woojeong.config.security.user.WjUserVo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommonDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public List<Map<String, Object>> getMainBanners () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getMainBanners");
    }

    public List<Map<String, Object>> getHistories () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getHistories");
    }

    public List<Map<String, Object>> getAllBanners () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getAllBanners");
    }

    public int insertBanner (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertBanner", params);
    }

    public int updateBannerOrder (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateBannerOrder", params);
    }

    public int toggleBannerValid (Long idx) {
        return sqlSessionTemplate.update("org.mybatis.example.common.toggleBannerValid", idx);
    }

    public int deleteBanner (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteBanner", idx);
    }

    // 섬기는 사람들
    public List<Map<String, Object>> getStaff () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getStaff");
    }
    public int insertStaff (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertStaff", params);
    }
    public int updateStaff (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateStaff", params);
    }
    public int deleteStaffSoft (Long idx) {
        return sqlSessionTemplate.update("org.mybatis.example.common.deleteStaffSoft", idx);
    }
    public int updateStaffSortOrder (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateStaffSortOrder", params);
    }
    public int updateCategoryOrd (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateCategoryOrd", params);
    }
    public int renameCategory (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.renameCategory", params);
    }

    // 교육부서
    public List<Map<String, Object>> getDepartmentList () {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getDepartmentList");
    }
    public Map<String, Object> getDepartment (String deptKey) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.common.getDepartment", deptKey);
    }
    public int updateDepartment (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartment", params);
    }
    public List<Map<String, Object>> getDepartmentStaff (String deptKey) {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getDepartmentStaff", deptKey);
    }
    public int insertDepartmentStaff (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertDepartmentStaff", params);
    }
    public int updateDepartmentStaff (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentStaff", params);
    }
    public int deleteDepartmentStaff (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteDepartmentStaff", idx);
    }
    public List<Map<String, Object>> getDepartmentTeachers (String deptKey) {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getDepartmentTeachers", deptKey);
    }
    public int insertDepartmentTeacher (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertDepartmentTeacher", params);
    }
    public int updateDepartmentTeacher (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentTeacher", params);
    }
    public int deleteDepartmentTeacher (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteDepartmentTeacher", idx);
    }
    // 부서 섹션
    public List<Map<String, Object>> getDepartmentSections (String deptKey) {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getDepartmentSections", deptKey);
    }
    public int insertDepartmentSection (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertDepartmentSection", params);
    }
    public int updateDepartmentSection (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentSection", params);
    }
    public int deleteDepartmentSection (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteDepartmentSection", idx);
    }
    public int updateDepartmentSectionSort (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentSectionSort", params);
    }

    public int insertDepartment (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertDepartment", params);
    }
    public int updateDepartmentMeta (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentMeta", params);
    }
    public int deleteDepartmentSoft (String deptKey) {
        return sqlSessionTemplate.update("org.mybatis.example.common.deleteDepartmentSoft", deptKey);
    }

    public int updateDepartmentStaffSort (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentStaffSort", params);
    }
    public int updateDepartmentTeacherSort (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentTeacherSort", params);
    }

    // 부서 이미지
    public List<Map<String, Object>> getDepartmentImages (String deptKey) {
        return sqlSessionTemplate.selectList("org.mybatis.example.common.getDepartmentImages", deptKey);
    }
    public int insertDepartmentImage (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.common.insertDepartmentImage", params);
    }
    public int deleteDepartmentImage (Long idx) {
        return sqlSessionTemplate.delete("org.mybatis.example.common.deleteDepartmentImage", idx);
    }
    public int updateDepartmentImageSort (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.common.updateDepartmentImageSort", params);
    }
}
