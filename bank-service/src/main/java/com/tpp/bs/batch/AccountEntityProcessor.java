package com.tpp.bs.batch;

import com.tpp.bs.account_adapter.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEntityProcessor implements ItemProcessor<AccountEntity, AccountEntity> {

    @Override
    public AccountEntity process(AccountEntity accountEntity) {
        log.info("AccountNumber : {}, balance: {}", accountEntity.getIdentification(), accountEntity.getBalance());
        return accountEntity;
    }
}
