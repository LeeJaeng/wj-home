package org.woojeong.api.v1.board;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.woojeong.api.v1.board.dto.CommunityBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.api.v1.board.vo.CommunityBoardListPaging;
import org.woojeong.api.v1.board.vo.WorshipBoardListPaging;
import org.woojeong.api.v1.board.vo.WorshipBoardVo;
import org.woojeong.config.security.user.WjUserInfo;
import org.woojeong.util.S3Uploader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final S3Uploader s3Uploader;
    private final BoardDao boardDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Integer CNT_PER_PAGE = 10;


    public boolean register(WorshipRegisterDTO worshipRegisterDTO) {
        boardDao.registerWorshipBoard(worshipRegisterDTO);
        return true;
    }

    public boolean editWorshipBoard(WorshipRegisterDTO worshipRegisterDTO) {
        boardDao.editWorshipBoard(worshipRegisterDTO);
        return true;
    }

    public boolean deleteWorshipBoard(Long board_idx) {
        boardDao.deleteWorshipBoard(board_idx);
        return true;
    }

    public Map<String, Object> getWorshipBoardList(WjUserInfo activeUser, String type, Integer page, Boolean init) {
        Map<String, Object> retMap = new HashMap<>();
        WorshipBoardListDTO dto = new WorshipBoardListDTO();

        dto.setMy_user_idx(activeUser.getUser_idx());
        dto.setType(type);
        dto.setPage(page);
        dto.setStart((dto.getPage() - 1) *  CNT_PER_PAGE);
        dto.setCnt(CNT_PER_PAGE);

        switch (dto.getType()) {
            case "head-pastor":
                dto.setQuery("AND category = 1 ");
                break;
            case "sub-pastor":
                dto.setQuery("AND category = 2 ");
                break;
            case "sunday":
                dto.setQuery("AND category = 3 AND worship_type IN ('sun1', 'sun2', 'sun3', 'sun4') ");
                break;
            case "friday":
                dto.setQuery("AND category = 3 AND worship_type = 'fri' ");
                break;
            case "wednesday":
                dto.setQuery("AND category = 3 AND worship_type in ('wed1', 'wed2') ");
                break;
            case "dawn":
                dto.setQuery("AND category = 3 AND worship_type = 'dawn' ");
                break;
            case "praise":
                dto.setQuery("AND category = 4 ");
                break;
            case "etc":
                dto.setQuery("AND category = 3 AND worship_type = 'etc' ");
                break;
            case "home-worship":
                dto.setQuery("AND category not in (1, 2) ");
                break;

        }




        retMap.put("list", boardDao.getWorshipBoardList(dto));


        if (init) {
            WorshipBoardListPaging paging = new WorshipBoardListPaging();
            paging.setCurPage(1);
            paging.setCntPerPage(CNT_PER_PAGE);
            paging.setTotalCnt(boardDao.foundRows());
            retMap.put("paging", paging);
        }
        return retMap;
    }

    public Map<String, Object> getWorshipBoard(Long user_idx, Long idx) {
        Map<String, Object> params = new HashMap<>();
        params.put("my_user_idx", user_idx);
        params.put("board_idx", idx);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", boardDao.getWorshipBoard(params));

        return retMap;
    }



    public boolean registerCommunityBoard(List<MultipartFile> files, Map<String, Object> params) {
        boardDao.registerCommunityBoard(params);
        try {
            for (MultipartFile file : files) {
                String fileName = "images" + "/" + UUID.randomUUID() + file.getName();   // S3에 저장된 파일 이름
                String url = s3Uploader.upload(file, fileName);
                params.put("url", url);
                params.put("file_type", file.getContentType());
                params.put("aws_file_name", fileName);
                params.put("origin_file_name", file.getOriginalFilename());
                boardDao.registerCommunityBoardFiles(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Map<String, Object> getCommunityBoardList(WjUserInfo activeUser, String type, Integer page, Boolean init) {
        Map<String, Object> retMap = new HashMap<>();
        CommunityBoardListDTO dto = new CommunityBoardListDTO();

        dto.setMy_user_idx(activeUser.getUser_idx());
        dto.setType(type);
        dto.setPage(page);
        dto.setStart((dto.getPage() - 1) *  CNT_PER_PAGE);
        dto.setCnt(CNT_PER_PAGE);

        switch (dto.getType()) {
            case "notice":
                dto.setQuery("AND category = 1 ");
                break;
            case "paper":
                dto.setQuery("AND category = 2 ");
                break;
            case "photo":
                dto.setQuery("AND category = 3 ");
                break;
            case "file":
                dto.setQuery("AND category = 4 ");
                break;
        }




        retMap.put("list", boardDao.getCommunityBoardList(dto));


        if (init) {
            CommunityBoardListPaging paging = new CommunityBoardListPaging();
            paging.setCurPage(1);
            paging.setCntPerPage(CNT_PER_PAGE);
            paging.setTotalCnt(boardDao.foundRows());
            retMap.put("paging", paging);
        }
        return retMap;
    }

    public Map<String, Object> getCommunityBoard(Long user_idx, Long idx) {
        Map<String, Object> params = new HashMap<>();
        params.put("my_user_idx", user_idx);
        params.put("board_idx", idx);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("data", boardDao.getCommunityBoard(params));
        retMap.put("files", boardDao.getCommunityBoardFiles(idx));

        return retMap;
    }

    public boolean deleteCommunityBoard(Long board_idx) {
        boardDao.deleteCommunityBoard(board_idx);
        return true;
    }
}
