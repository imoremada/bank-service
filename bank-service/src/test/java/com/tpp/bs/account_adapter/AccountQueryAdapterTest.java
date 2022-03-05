package com.tpp.bs.account_adapter;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.DailyInterest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountQueryAdapterTest {

    @InjectMocks
    private AccountQueryAdapter accountQueryAdapter;

    @Mock
    private AccountJpaRepository accountJpaRepository;

    @Mock
    private DailyInterestJpaRepository dailyInterestJpaRepository;

    @Test
    public void testShouldReturnEmptyWhenExceptionIsOccurredWhileFetchingAccount(){
        //Given
        String identification = "identification";
        Mockito.when(accountJpaRepository.findByIdentification(Mockito.anyString()))
                .thenThrow(new RuntimeException("exception occurred"));

        //When
        Optional<Account> account = accountQueryAdapter.findAccountBy(identification);

        //Then
        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    public void testShouldSuccessfullyReturnAccount(){
        //Given
        String identification = "identification";
        Mockito.when(accountJpaRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(AccountEntity.class)));

        //When
        Optional<Account> account = accountQueryAdapter.findAccountBy(identification);

        //Then
        Assertions.assertTrue(account.isPresent());
    }

    @Test
    public void testShouldReturnEmptyWhenExceptionIsOccurredWhileFetchingDailyInterest(){
        //Given
        String identification = "identification";
        Mockito.when(dailyInterestJpaRepository.findByIdIdentificationAndIdDate(Mockito.anyString(), Mockito.any(LocalDate.class)))
                .thenThrow(new RuntimeException("exception occurred"));

        //When
        Optional<DailyInterest> account = accountQueryAdapter.findDailyInterestBy(identification, LocalDate.now());

        //Then
        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    public void testShouldSuccessfullyReturnDailyInterest(){
        //Given
        String identification = "identification";
        DailyInterestEntity dailyInterestEntity = Mockito.mock(DailyInterestEntity.class);
        Mockito.when(dailyInterestEntity.getId()).thenReturn(Mockito.mock(DailyInterestId.class));
        Mockito.when(dailyInterestJpaRepository.findByIdIdentificationAndIdDate(Mockito.anyString(), Mockito.any(LocalDate.class)))
                .thenReturn(Optional.of(dailyInterestEntity));

        //When
        Optional<DailyInterest> account = accountQueryAdapter.findDailyInterestBy(identification, LocalDate.now());

        //Then
        Assertions.assertTrue(account.isPresent());
    }
}