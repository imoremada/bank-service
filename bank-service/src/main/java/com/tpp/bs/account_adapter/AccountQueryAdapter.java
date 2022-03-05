package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountQueryRepository;
import com.tpp.bs.account.DailyInterest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountQueryAdapter implements AccountQueryRepository {

    private final AccountJpaRepository accountJpaRepository;
    private final DailyInterestJpaRepository dailyInterestJpaRepository;

    @Override
    public Optional<Account> findAccountBy(String identification) {
        try {
            return accountJpaRepository.findByIdentification(identification)
                    .map(accountEntity ->
                            Optional.of(Account.builder()
                                    .openingDate(accountEntity.getOpeningDate())
                                    .balance(accountEntity.getBalance())
                                    .bsb(accountEntity.getBsb())
                                    .identification(accountEntity.getIdentification())
                                    .build())
                    )
                    .orElseGet(() -> {
                        log.error("No Account found for the given identification : {}", identification);
                        return Optional.empty();
                    });
        }catch (Exception e){
            log.error("Exception occurred while fetching account {}", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<DailyInterest> findDailyInterestBy(String identification, LocalDate date) {
        try{
            return dailyInterestJpaRepository.findByIdIdentificationAndIdDate(identification, date)
                    .map(dailyInterestEntity ->
                            Optional.of(DailyInterest.builder()
                                    .interest(dailyInterestEntity.getInterest())
                                    .localDateTime(dailyInterestEntity.getId().getDate())
                                    .dailyInterestRate(dailyInterestEntity.getInterestRate())
                                    .build())
                    )
                    .orElseGet(()->{
                        log.error("No Daily Interest found for the given identification : {}, date {}", identification, date);
                        return Optional.empty();
                    });
        }catch (Exception e){
            log.error("Exception occurred while fetching account {}", e);
            return Optional.empty();
        }
    }
}
