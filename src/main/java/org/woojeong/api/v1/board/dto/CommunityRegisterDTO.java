package org.woojeong.api.v1.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class CommunityRegisterDTO {
    private Long user_idx;
    private Integer category;
    private String title;
    private String content;
    private List<MultipartFile> files;
}
