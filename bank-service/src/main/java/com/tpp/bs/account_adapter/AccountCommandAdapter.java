package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountCommandRepository;
import com.tpp.bs.account.DailyInterest;
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
    private final DailyInterestJpaRepository dailyInterestJpaRepository;

    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean saveDailyInterest(Account account) {
        try {
            OffsetDateTime currentTime = dateTimeProvider.currentOffsetDateTime();
            accountJpaRepository.save(AccountEntity.builder()
                            .bsb(account.getBsb())
                            .identification(account.getIdentification())
                            .lastUpdatedTime(currentTime)
                            .createdTime(account.getCreatedDate())
                            .balance(account.getBalance())
                            .openingDate(account.getOpeningDate())
                    .build());
            log.info("Successfully save the account for bsp {}, openingDate: {}", account.getBsb(), account.getOpeningDate());
            return Boolean.TRUE;
        }catch (Exception e){
          log.error("Exception occurred while creating the account :{}", e);
          return Boolean.FALSE;
        }
    }

    @Override
    public boolean saveDailyInterest(DailyInterest dailyInterest) {
        try {
            dailyInterestJpaRepository.save(DailyInterestEntity.builder()
                            .interest(dailyInterest.getInterest())
                            .interestRate(dailyInterest.getDailyInterestRate())
                            .id(DailyInterestId.builder()
                                    .date(dailyInterest.getLocalDateTime())
                                    .identification(dailyInterest.getIdentification())
                                    .build())
                    .build());
            log.info("Successfully save daily interest for date: {}", dailyInterest.getLocalDateTime());
            return Boolean.TRUE;
        }catch (Exception e){
            log.error("Exception occurred while saving daily interest :{}", e);
            return Boolean.FALSE;
        }
    }
}
