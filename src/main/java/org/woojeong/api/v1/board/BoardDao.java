package org.woojeong.api.v1.board;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.woojeong.api.v1.board.dto.CommunityBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.api.v1.board.vo.CommunityBoardVo;
import org.woojeong.api.v1.board.vo.WorshipBoardVo;
import org.woojeong.config.security.user.WjUserVo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public Long foundRows () {
        return sqlSessionTemplate.selectOne("org.mybatis.example.board.foundRows");
    }


    public int registerWorshipBoard (WorshipRegisterDTO dto) {
        return sqlSessionTemplate.insert("org.mybatis.example.board.insertWorshipBoard", dto);
    }
    public int editWorshipBoard (WorshipRegisterDTO dto) {
        return sqlSessionTemplate.update("org.mybatis.example.board.editWorshipBoard", dto);
    }
    public int deleteWorshipBoard (Long board_idx) {
        return sqlSessionTemplate.update("org.mybatis.example.board.deleteWorshipBoard", board_idx);
    }
    public List<WorshipBoardVo> getWorshipBoardList (WorshipBoardListDTO dto) {
        return sqlSessionTemplate.selectList("org.mybatis.example.board.getWorshipBoardList", dto);
    }
    public WorshipBoardVo getWorshipBoard (Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.board.getWorshipBoard", params);
    }


    public int registerCommunityBoard (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.board.insertCommunityBoard", params);
    }
    public int editCommunityBoard (Map<String, Object> params) {
        return sqlSessionTemplate.update("org.mybatis.example.board.editCommunityBoard", params);
    }
    public Integer getFileOrd (Integer boardIdx) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.board.getFileOrd", boardIdx);
    }
    public int registerCommunityBoardFiles (Map<String, Object> params) {
        return sqlSessionTemplate.insert("org.mybatis.example.board.insertCommunityBoardFiles", params);
    }
    public int deleteFile (List<String> idxList) {
        return sqlSessionTemplate.update("org.mybatis.example.board.deleteFile", idxList);
    }
    public int deleteCommunityBoard (Long board_idx) {
        return sqlSessionTemplate.update("org.mybatis.example.board.deleteCommunityBoard", board_idx);
    }
    public List<CommunityBoardVo> getCommunityBoardList (CommunityBoardListDTO dto) {
        return sqlSessionTemplate.selectList("org.mybatis.example.board.getCommunityBoardList", dto);
    }
    public Map<String, Object> getCommunityBoard (Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.board.getCommunityBoard", params);
    }
    public List<Map<String, Object>>  getCommunityBoardFiles (Long board_idx) {
        return sqlSessionTemplate.selectList("org.mybatis.example.board.getCommunityBoardFiles", board_idx);
    }

}
