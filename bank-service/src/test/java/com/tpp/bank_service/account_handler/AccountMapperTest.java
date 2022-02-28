package com.tpp.bank_service.account_handler;

import com.tpp.bs.account.Account;
import com.tpp.bs.account_handler.AccountMapper;
import com.tpp.bs.account_handler.AccountOpenRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {
    @InjectMocks
    private AccountMapper accountMapper;

    @Test
    public void testShouldSuccessfullyMapAccountRequestToAccount(){
        //Given
        AccountOpenRequest accountOpenRequest = AccountOpenRequest.builder()
                .identification("identification")
                .bsb("bsp")
                .openingDate(LocalDate.now())
                .build();

        //When
        Account account = accountMapper.map(accountOpenRequest);

        //Then
        Assertions.assertEquals(accountOpenRequest.getBsb(), account.getBsb());
        Assertions.assertEquals(accountOpenRequest.getIdentification(), account.getIdentification());
        Assertions.assertEquals(accountOpenRequest.getOpeningDate(), account.getOpeningDate());
        Assertions.assertEquals(BigDecimal.ZERO, account.getBalance());
    }

}