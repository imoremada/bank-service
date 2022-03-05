package com.tpp.bs.account_adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyInterestJpaRepository extends JpaRepository<DailyInterestEntity, DailyInterestId>, JpaSpecificationExecutor<DailyInterestEntity> {
    Optional<DailyInterestEntity> findByIdIdentificationAndIdDate(String identification, LocalDate date);
}
