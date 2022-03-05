package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.DailyInterest;
import com.tpp.bs.common.DateTimeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountCommandAdapterTest {

    @InjectMocks
    private AccountCommandAdapter accountCommandAdapter;
    @Mock
    private AccountJpaRepository accountJpaRepository;
    @Mock
    private DateTimeProvider dateTimeProvider;
    @Mock
    private DailyInterestJpaRepository dailyInterestJpaRepository;

    @Test
    public void testShouldReturnFalseWhenExceptionIsOccurredWhileSavingAccount() {
        //Given
        Mockito.when(accountJpaRepository.save(any(AccountEntity.class))).thenThrow(new RuntimeException("Exception occurred"));

        //When
        Boolean isSuccess = accountCommandAdapter.saveDailyInterest(mock(Account.class));

        //Then
        Assertions.assertFalse(isSuccess);
    }

    @Test
    public void testShouldReturnTrueWhenAccountDataSaveIsSuccess() {
        //Given
        Account account = Account.builder().build();
        Mockito.when(accountJpaRepository.save(any(AccountEntity.class))).thenReturn(mock(AccountEntity.class));
        OffsetDateTime dateTime = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));
        Mockito.when(dateTimeProvider.currentOffsetDateTime()).thenReturn(dateTime);

        //When
        Boolean isSuccess = accountCommandAdapter.saveDailyInterest(account);

        //Then
        Assertions.assertTrue(isSuccess);
        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        Mockito.verify(accountJpaRepository, times(1)).save(argumentCaptor.capture());
        Assertions.assertEquals(dateTime, argumentCaptor.getValue().getCreatedTime());
    }

    @Test
    public void testShouldReturnFalseWhenExceptionIsOccurredWhileSavingInterest() {
        //Given
        Mockito.when(dailyInterestJpaRepository.save(any(DailyInterestEntity.class))).thenThrow(new RuntimeException("Exception occurred"));

        //When
        Boolean isSuccess = accountCommandAdapter.saveDailyInterest(mock(DailyInterest.class));

        //Then
        Assertions.assertFalse(isSuccess);
    }
}