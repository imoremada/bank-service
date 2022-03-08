package com.tpp.bs.batch;

import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_adapter.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyAccountInterestItemWriterTest {
    @InjectMocks
    private MonthlyAccountInterestItemWriter monthlyAccountInterestItemWriter;
    @Mock
    private AccountService accountService;
    @Spy
    private ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

    @Test
    public void testShouldSuccessfullyTriggerMonthlyAccountInterestCalculation() {
        //Given
        threadPoolTaskExecutor.initialize();
        List<AccountEntity> accountEntities = Collections.singletonList(AccountEntity.builder()
                .identification("identification")
                .build());

        //When
        monthlyAccountInterestItemWriter.write(accountEntities);

        //Then
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(15);
        threadPoolTaskExecutor.shutdown();
        verify(accountService, times(1)).calculateMonthlyInterest(anyString());
    }
}