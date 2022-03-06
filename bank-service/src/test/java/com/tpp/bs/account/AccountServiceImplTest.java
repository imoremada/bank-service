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
import java.time.*;
import java.util.Arrays;
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

    @Test
    public void testShouldNotCalculateMonthlyInterestIfExecutionDateIsNotTheLastDayOfMonth(){
        //Given
        String identification = "identification";
        LocalDate localDate = LocalDate.of(2022, 3, 5);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(1, 5));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        when(dateTimeProvider.currentOffsetDateTime()).thenReturn(offsetDateTime);

        //When
        Optional<Account> account = accountService.calculateMonthlyInterest(identification);

        //Then
        Assertions.assertTrue(account.isEmpty());
        verify(accountCommandRepository, never()).saveMonthlyInterest(any(MonthlyInterest.class));
        verify(accountQueryRepository, never()).findMonthlyInterestBy(anyString(), any(OffsetDateTime.class));
        verify(accountQueryRepository, never()).findAccountBy(anyString());
    }

    @Test
    public void testShouldNotCalculateMonthlyInterestIfMonthlyInterestIsAlreadyCalculated(){
        //Given
        String identification = "identification";
        LocalDate localDate = LocalDate.of(2022, 3, 31);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(1, 5));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        when(dateTimeProvider.currentOffsetDateTime()).thenReturn(offsetDateTime);
        Account account = Account.builder().build();
        when(accountQueryRepository.findAccountBy(anyString())).thenReturn(Optional.of(account));
        when(accountQueryRepository.findMonthlyInterestBy(anyString(), any(OffsetDateTime.class)))
                .thenReturn(Optional.of(mock(MonthlyInterest.class)));

        //When
        Optional<Account> updatedAccount = accountService.calculateMonthlyInterest(identification);

        //Then
        Assertions.assertFalse(updatedAccount.isEmpty());
        Assertions.assertEquals(account, updatedAccount.get());
        verify(accountQueryRepository, never()).findDailyInterestInMonth(anyString(), any(LocalDate.class));
        verify(accountCommandRepository, never()).updateAccountWithMonthlyInterest(any(Account.class), any(MonthlyInterest.class));
    }

    @Test
    public void testShouldSuccessfullyCalculateMonthlyInterest(){
        //Given
        String identification = "identification";
        LocalDate localDate = LocalDate.of(2022, 3, 31);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(1, 5));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        when(dateTimeProvider.currentOffsetDateTime()).thenReturn(offsetDateTime);
        Account account = Account.builder()
                .balance(BigDecimal.TEN)
                .identification(identification)
                .build();
        when(accountQueryRepository.findAccountBy(anyString())).thenReturn(Optional.of(account));
        when(accountQueryRepository.findMonthlyInterestBy(anyString(), any(OffsetDateTime.class)))
                .thenReturn(Optional.empty());
        DailyInterest dailyInterest1 = DailyInterest.builder()
                .interest(new BigDecimal("1.5"))
                .build();
        DailyInterest dailyInterest2 = DailyInterest.builder()
                .interest(new BigDecimal("1.6"))
                .build();
        BigDecimal expectedMonthlyInterest = dailyInterest1.getInterest().add(dailyInterest2.getInterest());
        BigDecimal expectedBalance = account.getBalance().add(dailyInterest1.getInterest()).add(dailyInterest2.getInterest());
        List<DailyInterest> dailyInterests = Arrays.asList(dailyInterest1, dailyInterest2);
        when(accountQueryRepository.findDailyInterestInMonth(anyString(), any(LocalDate.class))).thenReturn(dailyInterests);

        //When
        Optional<Account> updatedAccount = accountService.calculateMonthlyInterest(identification);

        //Then
        Assertions.assertFalse(updatedAccount.isEmpty());
        Assertions.assertEquals(account, updatedAccount.get());
        Assertions.assertEquals(expectedBalance, updatedAccount.get().getBalance());
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<MonthlyInterest> monthlyInterestArgumentCaptor = ArgumentCaptor.forClass(MonthlyInterest.class);
        verify(accountCommandRepository, times(1))
                .updateAccountWithMonthlyInterest(accountArgumentCaptor.capture(), monthlyInterestArgumentCaptor.capture());
        Assertions.assertEquals(expectedBalance, accountArgumentCaptor.getValue().getBalance());
        Assertions.assertEquals(expectedMonthlyInterest, monthlyInterestArgumentCaptor.getValue().getInterest());
    }
}