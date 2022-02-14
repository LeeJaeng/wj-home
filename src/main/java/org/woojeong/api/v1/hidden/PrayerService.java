package org.woojeong.api.v1.hidden;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woojeong.api.v1.hidden.dto.InsertPrayerContentDTO;
import org.woojeong.api.v1.hidden.vo.PrayerListVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrayerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PrayerDao dao;

    public Map<String, Object> getPrayerList(String groupKey) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", dao.getPrayerList(groupKey));
        return retMap;
    }

    @Transactional
    public Map<String, Object> insertPrayerContent(InsertPrayerContentDTO dto) {
        Map<String, Object> retMap = new HashMap<>();
        dao.prevPrayerContentDelete(dto);
        if (dto.getContent() != null && !dto.getContent().equals("")) {
            dao.insertPrayerContent(dto);
        }
        return retMap;
    }

}
