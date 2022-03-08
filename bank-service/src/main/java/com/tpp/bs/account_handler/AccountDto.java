package com.tpp.bs.account_handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Valid
public class AccountDto {
    @NotBlank
    private String bsb;
    @NotBlank
    private String identification;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openingDate;
    private BigDecimal balance;
}
