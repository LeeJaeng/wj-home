package org.woojeong.api.v1.common;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woojeong.api.v1.account.AccountService;

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


}
