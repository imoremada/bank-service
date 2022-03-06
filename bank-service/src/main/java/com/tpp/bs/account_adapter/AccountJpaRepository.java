package com.tpp.bs.account_adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByIdentification(String identification);

    @Modifying
    @Query("update AccountEntity a set a.balance = :balance, a.lastCalculatedInterest=:interest , a.lastUpdatedTime =:lastUpdatedDateTime where a.identification = :identification")
    void updateBalance(@Param("identification") String identification,
                       @Param("interest") BigDecimal interest,
                       @Param("balance") BigDecimal balance,
                       @Param("lastUpdatedDateTime")OffsetDateTime offsetDateTime);

}
