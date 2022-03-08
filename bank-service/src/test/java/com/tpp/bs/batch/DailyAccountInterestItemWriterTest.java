package com.tpp.bs.batch;

import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_adapter.AccountEntity;
import com.tpp.bs.common.DateTimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAccountInterestItemWriterTest {

    @InjectMocks
    private DailyAccountInterestItemWriter dailyAccountInterestItemWriter;

    @Mock
    private AccountService accountService;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Test
    public void testShouldSuccessfullyTriggerDailyAccountInterestCalculation(){
        //Given
        List<AccountEntity> accountEntities = Collections.singletonList(mock(AccountEntity.class));
        when(dateTimeProvider.currentOffsetDateTime()).thenReturn(OffsetDateTime.now());

        //When
        dailyAccountInterestItemWriter.write(accountEntities);

        //Then
        verify(accountService, times(1)).calculateDailyAccruedInterest(anyList(), any(LocalDate.class));
    }
}