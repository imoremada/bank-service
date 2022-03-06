package com.tpp.bs.account_adapter;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MonthlyInterestId implements Serializable {
    private int year;
    private int month;
    private String identification;
}
