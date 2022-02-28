package com.tpp.bs.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Override
    public Boolean processAccountOpening(Account account) {
        return Boolean.FALSE;
    }
}
