package org.woojeong.api.v1.account;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woojeong.api.v1.account.dto.RegisterDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountService accountService;


    @PostMapping(value = "/public/account/register")
    public ResponseEntity register (@RequestBody RegisterDTO registerDTO) {
        if (accountService.register(registerDTO)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
