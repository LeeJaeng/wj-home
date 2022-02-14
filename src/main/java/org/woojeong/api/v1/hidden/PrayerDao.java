package org.woojeong.api.v1.hidden;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.woojeong.api.v1.board.dto.CommunityBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.api.v1.board.vo.CommunityBoardVo;
import org.woojeong.api.v1.board.vo.WorshipBoardVo;
import org.woojeong.api.v1.hidden.dto.InsertPrayerContentDTO;
import org.woojeong.api.v1.hidden.vo.PrayerListVo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PrayerDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public List<PrayerListVo> getPrayerList (String groupKey) {
        return sqlSessionTemplate.selectList("org.mybatis.example.hidden.getPrayerList", groupKey);
    }

    public String getPrayerGroupName (String groupKey) {
        return sqlSessionTemplate.selectOne("org.mybatis.example.hidden.getPrayerGroupName", groupKey);
    }

    public int insertPrayerContent (InsertPrayerContentDTO dto) {
        return sqlSessionTemplate.insert("org.mybatis.example.hidden.insertPrayerContent", dto);
    }

    public int prevPrayerContentDelete (InsertPrayerContentDTO dto) {
        return sqlSessionTemplate.update("org.mybatis.example.hidden.prevPrayerContentDelete", dto);
    }
}
