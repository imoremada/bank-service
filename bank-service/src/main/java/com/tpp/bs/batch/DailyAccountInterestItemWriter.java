package com.tpp.bs.batch;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_adapter.AccountEntity;
import com.tpp.bs.common.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DailyAccountInterestItemWriter implements ItemWriter<AccountEntity> {
    private final AccountService accountService;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void write(List<? extends AccountEntity> accountEntities) {
        log.info("Going to initiate daily interest calculation for {} accounts", accountEntities.size());
        List<Account> accounts = accountEntities.stream()
                .map(accountEntity->Account.builder()
                        .identification(accountEntity.getIdentification())
                        .balance(accountEntity.getBalance())
                        .bsb(accountEntity.getBsb())
                        .build())
                .collect(Collectors.toList());
        accountService.calculateDailyAccruedInterest(accounts, dateTimeProvider.currentOffsetDateTime().toLocalDate());
    }
}
