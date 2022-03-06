package com.tpp.bs.account_adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MonthlyInterestJpaRepository extends JpaRepository<MonthlyInterestEntity, MonthlyInterestId>, JpaSpecificationExecutor<MonthlyInterestEntity>  {

    Optional<MonthlyInterestEntity> findByIdIdentificationAndIdYearAndIdMonth(String identification, int year, int month);

}
