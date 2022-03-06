package com.tpp.bs.account_adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyInterestJpaRepository extends JpaRepository<DailyInterestEntity, DailyInterestId>, JpaSpecificationExecutor<DailyInterestEntity> {
    Optional<DailyInterestEntity> findByIdIdentificationAndIdDate(String identification, LocalDate date);

    @Query("select d from DailyInterestEntity d where d.id.identification=?1 and year(d.id.date) = ?2 and month(d.id.date) = ?3")
    List<DailyInterestEntity> findByYearAndMonth(@Param("identification") String identification, @Param("year") int year, @Param("month") int month);
}
