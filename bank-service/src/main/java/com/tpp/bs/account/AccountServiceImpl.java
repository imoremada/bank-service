package com.tpp.bs.account;

import com.tpp.bs.common.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.tpp.bs.utility.DateTimeUtil.getDaysInMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final int HUNDRED = 100;
    @Value("${monthlyInterestRate:1}")
    private BigDecimal monthlyInterestRate;
    private final AccountCommandRepository accountCommandRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final DateTimeProvider dateTimeProvider;
    private final Executor executor;

    @Override
    public Boolean processAccountOpening(Account account) {
        account.setCreatedDate(dateTimeProvider.currentOffsetDateTime());
        return accountCommandRepository.saveAccount(account);
    }

    @Override
    public List<Account> calculateDailyAccruedInterest(List<Account> accounts, LocalDate balanceDate) {
        return accounts.stream()
                .map(account -> CompletableFuture.supplyAsync(() -> processDailyAccruedInterestCalculation(balanceDate, account), executor))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join) //wait for each
                .collect(Collectors.toList());
    }

    private Account processDailyAccruedInterestCalculation(LocalDate balanceDate, Account account) {
        return accountQueryRepository.findAccountBy(account.getIdentification())
                .map(fetchedAccount ->
                        accountQueryRepository.findDailyInterestBy(fetchedAccount.getIdentification(), balanceDate)
                                .map(dailyInterest -> {
                                    log.warn("Daily interest has already been calculated for bsp: {}", account.getBsb());
                                    return fetchedAccount;
                                }).orElseGet(() -> {
                                    BigDecimal calculatedDailyInterest = calculateDailyAccruedInterestForTheDate(balanceDate, fetchedAccount);
                                    fetchedAccount.setBalance(fetchedAccount.getBalance().add(calculatedDailyInterest));
                                    DailyInterest dailyInterest = DailyInterest.builder()
                                            .dailyInterestRate(monthlyInterestRate)
                                            .interest(calculatedDailyInterest)
                                            .localDateTime(dateTimeProvider.now().toLocalDate())
                                            .identification(account.getIdentification())
                                            .build();
                                    saveAccruedInterest(fetchedAccount, dailyInterest);
                                    return fetchedAccount;
                                })
                ).orElseGet(() -> {
                    log.error("No matching Account {} found to calculate the daily accrued interest", account.getIdentification());
                    return account;
                });
    }

    public void saveAccruedInterest(Account account, DailyInterest dailyInterest) {
        accountCommandRepository.saveDailyAccountInterest(dailyInterest);
        log.info("Successfully updated the account with the daily interest bsp :{}", account.getBsb());
    }

    private BigDecimal calculateDailyAccruedInterestForTheDate(LocalDate balanceDate, Account fetchedAccount) {
        int daysInMonth = getDaysInMonth(balanceDate);
        BigDecimal currentBalance = fetchedAccount.getBalance();
        return currentBalance.multiply(monthlyInterestRate)
                .divide(new BigDecimal(daysInMonth * HUNDRED), 2, RoundingMode.HALF_DOWN);
    }
}
