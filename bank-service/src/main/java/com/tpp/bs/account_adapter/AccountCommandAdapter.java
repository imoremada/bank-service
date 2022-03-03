package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountCommandRepository;
import com.tpp.bs.common.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountCommandAdapter implements AccountCommandRepository {
    private final AccountJpaRepository accountJpaRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public Boolean create(Account account) {
        try {
            OffsetDateTime currentTime = dateTimeProvider.currentOffsetDateTime();
            accountJpaRepository.save(AccountEntity.builder()
                            .bsb(account.getBsb())
                            .identification(account.getIdentification())
                            .lastUpdatedTime(currentTime)
                            .createdTime(currentTime)
                            .balance(account.getBalance())
                            .openingDate(account.getOpeningDate())
                    .build());
            log.info("Successfully processed the account opening for bsp {}, openingDate: {}", account.getBsb(), account.getOpeningDate());
            return Boolean.TRUE;
        }catch (Exception e){
          log.error("Exception occurred while creating the account :{}", e);
          return Boolean.FALSE;
        }
    }
}
