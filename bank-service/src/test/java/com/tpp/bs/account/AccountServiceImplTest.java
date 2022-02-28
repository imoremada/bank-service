package com.tpp.bs.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void testShouldReturnFalseWhenCallingAccountOpening(){
        //Given
        Account account = Mockito.mock(Account.class);

        //When
        Boolean isAccountOpened = accountService.processAccountOpening(account);

        //Then
        Assertions.assertFalse(isAccountOpened);
    }
}