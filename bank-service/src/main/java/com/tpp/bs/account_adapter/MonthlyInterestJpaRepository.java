package com.tpp.bs.account_adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MonthlyInterestJpaRepository extends JpaRepository<MonthlyInterestEntity, MonthlyInterestId>, JpaSpecificationExecutor<MonthlyInterestEntity>  {
}
