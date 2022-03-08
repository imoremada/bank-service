package com.tpp.bs.batch;

import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_adapter.AccountEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class MonthlyAccountInterestItemWriter implements ItemWriter<AccountEntity> {
    private final AccountService accountService;
    private final Executor executor;

    @Override
    public void write(List<? extends AccountEntity> accountEntities) {
        log.info("Going to initiate monthly interest calculation for {} accounts", accountEntities.size());
        accountEntities.stream()
                .map(account -> CompletableFuture.supplyAsync(() -> accountService.calculateMonthlyInterest(account.getIdentification()), executor))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join) //wait for each
                .collect(Collectors.toList());
        }
}
