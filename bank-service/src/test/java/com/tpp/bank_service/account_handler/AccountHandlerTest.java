package com.tpp.bank_service.account_handler;

import com.tpp.bs.account.Account;
import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_handler.AccountHandler;
import com.tpp.bs.account_handler.AccountMapper;
import com.tpp.bs.account_handler.AccountOpenRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
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
        AccountOpenRequest accountOpenRequest = mock(AccountOpenRequest.class);
        when(accountMapper.map(any(AccountOpenRequest.class))).thenReturn(mock(Account.class));
        when(accountService.processAccountOpening(any(Account.class))).thenReturn(Boolean.TRUE);

        //When
        Boolean isSuccess = accountHandler.openAccount(accountOpenRequest);

        //Then
        Assertions.assertTrue(isSuccess);
    }
}