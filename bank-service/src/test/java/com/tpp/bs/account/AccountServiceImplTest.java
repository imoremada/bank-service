package com.tpp.bs.account;

import com.tpp.bs.common.DateTimeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountCommandRepository accountCommandRepository;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Mock
    private AccountQueryRepository accountQueryRepository;

    @Spy
    private ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();


    @Test
    public void testShouldReturnTrueWhenCallingAccountOpening() {
        //Given
        Account account = Mockito.mock(Account.class);
        when(dateTimeProvider.currentOffsetDateTime()).thenReturn(OffsetDateTime.now());
        when(accountCommandRepository.saveAccount(any(Account.class))).thenReturn(Boolean.TRUE);

        //When
        Boolean isAccountOpened = accountService.processAccountOpening(account);

        //Then
        Assertions.assertTrue(isAccountOpened);
    }

    @Test
    public void testShouldSuccessfullyCalculateTheInterestForTheGivenAccount() {
        //Given
        threadPoolTaskExecutor.initialize();
        ReflectionTestUtils.setField(accountService, "monthlyInterestRate", new BigDecimal("3"));
        LocalDate balanceDate = LocalDate.of(2022, 03, 05);
        String identification = "identification";
        List<Account> accounts = Collections.singletonList(Account.builder()
                .identification(identification)
                .build());
        BigDecimal currentBalance = new BigDecimal("1000");
        when(accountQueryRepository.findAccountBy(anyString())).thenReturn(Optional.of(Account.builder()
                .balance(currentBalance)
                .identification(identification)
                .bsb("bsp")
                .build()));
        when(accountQueryRepository.findDailyInterestBy(anyString(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(dateTimeProvider.now()).thenReturn(ZonedDateTime.now());
        when(accountCommandRepository.saveDailyAccountInterest(any(DailyInterest.class))).thenReturn(Boolean.TRUE);

        //When
        List<Account> updatedAccounts = accountService.calculateDailyAccruedInterest(accounts, balanceDate);

        //Then
        verify(threadPoolTaskExecutor, times(1)).execute(any());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(15);
        threadPoolTaskExecutor.shutdown();
        Assertions.assertFalse(updatedAccounts.isEmpty());
        ArgumentCaptor<DailyInterest> interestArgumentCaptor = ArgumentCaptor.forClass(DailyInterest.class);
        verify(accountCommandRepository, times(1)).saveDailyAccountInterest(interestArgumentCaptor.capture());
        Assertions.assertEquals(new BigDecimal("0.97"), interestArgumentCaptor.getValue().getInterest());
        Assertions.assertEquals(new BigDecimal("1000.97"), updatedAccounts.get(0).getBalance());
    }

    @Test
    public void testShouldNotCalculateTheInterestForTheGivenAccountIfInterestIsAlreadyCalculated() {
        //Given
        threadPoolTaskExecutor.initialize();
        ReflectionTestUtils.setField(accountService, "monthlyInterestRate", new BigDecimal("3"));
        LocalDate balanceDate = LocalDate.of(2022, 03, 05);
        String identification = "identification";
        List<Account> accounts = Collections.singletonList(Account.builder()
                .identification(identification)
                .build());
        BigDecimal currentBalance = new BigDecimal("1000");
        Account accountSavedInDB = Account.builder()
                .balance(currentBalance)
                .identification(identification)
                .bsb("bsp")
                .build();
        when(accountQueryRepository.findAccountBy(anyString())).thenReturn(Optional.of(accountSavedInDB));
        when(accountQueryRepository.findDailyInterestBy(anyString(), any(LocalDate.class))).thenReturn(Optional.of(DailyInterest.builder()
                .build()));

        //When
        List<Account> updatedAccounts = accountService.calculateDailyAccruedInterest(accounts, balanceDate);

        //Then
        verify(threadPoolTaskExecutor, times(1)).execute(any());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(15);
        threadPoolTaskExecutor.shutdown();
        Assertions.assertFalse(updatedAccounts.isEmpty());
        verify(accountCommandRepository, never()).saveDailyAccountInterest(any(DailyInterest.class));
        Assertions.assertEquals(accountSavedInDB.getBalance(), updatedAccounts.get(0).getBalance());
    }


    @Test
    public void testShouldReturnSameIncomingRequestWhenAccountNotPresent() {
        //Given
        threadPoolTaskExecutor.initialize();
        ReflectionTestUtils.setField(accountService, "monthlyInterestRate", new BigDecimal("3"));
        LocalDate balanceDate = LocalDate.of(2022, 03, 05);
        String identification = "identification";
        Account incomingAccount = Account.builder()
                .identification(identification)
                .balance(BigDecimal.TEN)
                .build();
        List<Account> accounts = Collections.singletonList(incomingAccount);
        when(accountQueryRepository.findAccountBy(anyString())).thenReturn(Optional.empty());

        //When
        List<Account> updatedAccounts = accountService.calculateDailyAccruedInterest(accounts, balanceDate);

        //Then
        verify(threadPoolTaskExecutor, times(1)).execute(any());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(15);
        threadPoolTaskExecutor.shutdown();
        Assertions.assertFalse(updatedAccounts.isEmpty());
        verify(accountQueryRepository, never()).findDailyInterestBy(anyString(), any(LocalDate.class));
        verify(accountCommandRepository, never()).saveDailyAccountInterest(any(DailyInterest.class));
        Assertions.assertEquals(incomingAccount.getBalance(), updatedAccounts.get(0).getBalance());
    }
}