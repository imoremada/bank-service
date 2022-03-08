package com.tpp.bank_service.account_handler;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_handler.AccountBalanceCalculationRequest;
import com.tpp.bs.account_handler.AccountHandler;
import com.tpp.bs.account_handler.AccountMapper;
import com.tpp.bs.account_handler.AccountDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountHandlerTest {

    @InjectMocks
    private AccountHandler accountHandler;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountService accountService;

    @Test
    public void testShouldSuccessfullyHandleAccountOpeningRequest() {
        //Given
        AccountDto accountDto = mock(AccountDto.class);
        when(accountMapper.map(any(AccountDto.class))).thenReturn(mock(Account.class));
        when(accountService.processAccountOpening(any(Account.class))).thenReturn(Boolean.TRUE);

        //When
        Boolean isSuccess = accountHandler.openAccount(accountDto);

        //Then
        Assertions.assertTrue(isSuccess);
    }

    @Test
    public void testShouldReturnFalseHandleAccountOpeningRequest() {
        //Given
        AccountBalanceCalculationRequest accountBalanceCalculationRequest = mock(AccountBalanceCalculationRequest.class);

        //When
        List<AccountDto> accountDtos = accountHandler.processEndOfTheDayBalance(accountBalanceCalculationRequest);

        //Then
        Assertions.assertTrue(accountDtos.isEmpty());
    }

    @Test
    public void testShouldSuccessfullyHandleDailyBalanceCalculation(){
        AccountBalanceCalculationRequest accountBalanceCalculationRequest = AccountBalanceCalculationRequest.builder()
                .balanceDate(LocalDate.now())
                .accounts(Collections.singletonList(mock(AccountDto.class)))
                .build();

        when(accountService.calculateDailyAccruedInterest(anyList(), any(LocalDate.class))).thenReturn(Collections.singletonList(mock(Account.class)));
        when(accountMapper.map(any(AccountDto.class))).thenReturn(mock(Account.class));

        //When
        List<AccountDto> accountDtos = accountHandler.processEndOfTheDayBalance(accountBalanceCalculationRequest);

        //Then
        Assertions.assertFalse(accountDtos.isEmpty());
    }
}