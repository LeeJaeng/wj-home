package org.woojeong.api.v1.board;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.woojeong.api.v1.board.dto.CommunityBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipBoardListDTO;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.api.v1.board.vo.CommunityBoardListPaging;
import org.woojeong.api.v1.board.vo.WorshipBoardListPaging;
import org.woojeong.api.v1.board.vo.WorshipBoardVo;
import org.woojeong.config.security.user.WjUserInfo;
import org.woojeong.util.S3Uploader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final S3Uploader s3Uploader;
    private final BoardDao boardDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Integer CNT_PER_PAGE = 12;


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
            case "old-pastor":
                dto.setQuery("AND category = 5 ");
                break;
            case "head-pastor-main":
                dto.setQuery("AND category = 1 AND worship_type IN ('sun3', 'sun4', 'fri') ");
                break;
            case "sub-pastor":
                dto.setQuery("AND category = 2 ");
                break;
            case "main-sermon-sun4":
                dto.setQuery("AND category = 2 AND worship_type = 'sun4'");
                break;
            case "main-sun4":
                dto.setQuery("AND category = 3 AND worship_type = 'sun4'");
                break;
            case "sunday":
                dto.setQuery("AND category = 3 AND worship_type IN ('sun1', 'sun2', 'sun3', 'sun4') ");
                break;
            case "friday":
                dto.setQuery("AND category = 3 AND worship_type = 'fri' ");
                break;
            case "head-pastor-friday":
                dto.setQuery("AND category = 1 AND worship_type = 'fri' ");
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



    @Transactional
    public boolean registerCommunityBoard(List<MultipartFile> files, Map<String, Object> params) {
        boardDao.registerCommunityBoard(params);
        if (files == null) {
            return true;
        }
        try {
            int ord  = 0;
            int i = 0;
            for (MultipartFile file : files) {
                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstubvwxyz") + "_" + file.getOriginalFilename();
                String fileName = "images" + "/files/" + rand;   // S3에 저장된 파일 이름
                String thumbUrl = "";


                String url = s3Uploader.upload(file, fileName);
                params.put("url", url);
                params.put("ord", ord++);
                params.put("file_type", file.getContentType());
                params.put("aws_file_name", fileName);
                params.put("origin_file_name", file.getOriginalFilename());
                boardDao.registerCommunityBoardFiles(params);

                if (i == 0 && file.getContentType().contains("image") ) {
                    String thumbName = "images" + "/thumbs/" + rand;   // S3에 저장된 파일 이름
                    File thumbs = makeThumbnail(file, rand, FilenameUtils.getExtension(file.getOriginalFilename()));
                    thumbUrl = s3Uploader.upload(thumbs, thumbName);
                    thumbs.delete();
                    params.put("url", thumbUrl);
                    params.put("ord", -1);
                    params.put("aws_file_name", thumbName);
                    params.put("origin_file_name", file.getOriginalFilename());
                    i++;
                    boardDao.registerCommunityBoardFiles(params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public boolean editCommunityBoard(List<MultipartFile> files, Map<String, Object> params) {
        boardDao.editCommunityBoard(params);
        if (params.containsKey("deleted_files")) {
            String[] deletedFiles = (String[])params.get("deleted_files");
            boardDao.deleteFile(Arrays.asList(deletedFiles));
        }
        if (files == null) {
            return true;
        }
        Integer ord = boardDao.getFileOrd((Integer)params.get("board_idx"));
        ord = ord == null ? 0 : ord;
        try {
            int i = 0;
            for (MultipartFile file : files) {
                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                String rand = RandomStringUtils.random(8, "0123456789abcdefghijklmnopqrstubvwxyz") + "_" + file.getOriginalFilename();
                String fileName = "images" + "/files/" + rand;   // S3에 저장된 파일 이름
                String thumbUrl = "";


                String url = s3Uploader.upload(file, fileName);
                params.put("url", url);
                params.put("ord", ++ord);
                params.put("file_type", file.getContentType());
                params.put("aws_file_name", fileName);
                params.put("origin_file_name", file.getOriginalFilename());
                boardDao.registerCommunityBoardFiles(params);

                if (i == 0 && file.getContentType().contains("image") ) {
                    String thumbName = "images" + "/thumbs/" + rand;   // S3에 저장된 파일 이름
                    File thumbs = makeThumbnail(file, rand, FilenameUtils.getExtension(file.getOriginalFilename()));
                    thumbUrl = s3Uploader.upload(thumbs, thumbName);
                    thumbs.delete();
                    params.put("url", thumbUrl);
                    params.put("ord", -1);
                    params.put("aws_file_name", thumbName);
                    params.put("origin_file_name", file.getOriginalFilename());
                    i++;
                    boardDao.registerCommunityBoardFiles(params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // MultipartFile 을 file로 변형 후 로컬로 저장, 파일객체를 리턴
    public Optional<File> convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private File makeThumbnail(MultipartFile origin, String fileName, String fileExt) throws Exception {

        // 저장된 원본파일로부터 BufferedImage 객체를 생성합니다.
        BufferedImage srcImg = ImageIO.read(origin.getInputStream());

        // 썸네일의 너비와 높이 입니다.
        int dw = 400, dh = 400;

        // 원본 이미지의 너비와 높이 입니다.
        int ow = srcImg.getWidth();
        int oh = srcImg.getHeight();

        // 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
        int nw = ow;
        int nh = (ow * dh) / dw;

        // 계산된 높이가 원본보다 높다면 crop이 안되므로
        // 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
        if(nh > oh) {
            nw = (oh * dw) / dh;
            nh = oh;
        }

        // 계산된 크기로 원본이미지를 가운데에서 crop 합니다.
        BufferedImage cropImg = Scalr.crop(srcImg, (ow-nw)/2, (oh-nh)/2, nw, nh);

        // crop된 이미지로 썸네일을 생성합니다.
        BufferedImage destImg = Scalr.resize(cropImg, dw, dh);

        // 썸네일을 저장합니다. 이미지 이름 앞에 "THUMB_" 를 붙여 표시했습니다.
        File thumbFile = new File(fileName);
        ImageIO.write(destImg, fileExt, thumbFile);

        return thumbFile;
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
            case "edit":
                dto.setQuery("AND category = 5 ");
                break;
            case "mp3":
                dto.setQuery("AND category = 6 ");
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
