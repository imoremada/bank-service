package com.tpp.bs.account_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountHandler accountHandler;

    @PostMapping("/processAccountOpening")
    public Boolean processAccountOpening(@Valid @RequestBody AccountOpenRequest accountOpenRequest) {
        return accountHandler.openAccount(accountOpenRequest);
    }
}
