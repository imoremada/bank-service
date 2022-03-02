package com.tpp.bs.account_handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Valid
public class AccountRequest {
    @NotBlank
    private String bsb;
    @NotBlank
    private String identification;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openingDate;
}
