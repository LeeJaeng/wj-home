package org.woojeong.api.v1.hidden;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.woojeong.api.v1.board.BoardService;
import org.woojeong.api.v1.board.dto.WorshipRegisterDTO;
import org.woojeong.api.v1.hidden.dto.InsertPrayerContentDTO;
import org.woojeong.config.security.user.WjUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PrayerRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PrayerService prayerService;


    @GetMapping(value = "/user/hidden/prayer/{group_key}")
    public ResponseEntity getPrayerList(@AuthenticationPrincipal WjUserInfo activeUser,
                                              @PathVariable(name = "group_key") String groupKey
    ) {
        return ResponseEntity.ok(prayerService.getPrayerList(groupKey));
    }
    @PostMapping(value = "/user/hidden/prayer/content")
    public ResponseEntity insertPrayerContent(@AuthenticationPrincipal WjUserInfo activeUser,
                                              @RequestBody InsertPrayerContentDTO dto
                                              ) {
        return ResponseEntity.ok(prayerService.insertPrayerContent(dto));
    }
}
