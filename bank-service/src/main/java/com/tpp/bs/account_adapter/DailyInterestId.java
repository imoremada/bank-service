package com.tpp.bs.account_adapter;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@IdClass(LocalDate.class)
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DailyInterestId implements Serializable {
    private String identification;
    private LocalDate date;
}
