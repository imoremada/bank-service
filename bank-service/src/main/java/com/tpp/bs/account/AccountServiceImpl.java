package com.tpp.bs.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountCommandRepository accountCommandRepository;

    @Override
    public Boolean processAccountOpening(Account account) {
        return accountCommandRepository.create(account);
    }
}
