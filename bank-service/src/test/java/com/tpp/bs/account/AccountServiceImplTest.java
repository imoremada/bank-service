package com.tpp.bs.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountCommandRepository accountCommandRepository;

    @Test
    public void testShouldReturnTrueWhenCallingAccountOpening(){
        //Given
        Account account = Mockito.mock(Account.class);
        Mockito.when(accountCommandRepository.create(any(Account.class))).thenReturn(Boolean.TRUE);

        //When
        Boolean isAccountOpened = accountService.processAccountOpening(account);

        //Then
        Assertions.assertTrue(isAccountOpened);
    }
}