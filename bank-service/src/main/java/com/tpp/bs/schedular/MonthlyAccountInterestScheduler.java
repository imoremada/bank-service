package com.tpp.bs.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static javax.batch.runtime.BatchStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class MonthlyAccountInterestScheduler {

    private final @Qualifier("monthlyAccountInterestJob") Job monthlyAccountInterestJob;

    @Autowired
    private JobRepository jobRepository;

    @Scheduled(cron = "${monthlyInterestCronExpression}")//Last day of month at 11pm
    public void monthlyInterestCalculationScheduler() {
        log.info("Monthly Account Interest Calculation Scheduler has been started");
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        try {
            SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
            try {
                jobLauncher.afterPropertiesSet();
            } catch (Exception e) {
                log.error("Exception occurred when scheduling monthly interest calculation scheduler ", e);
            }
            JobExecution execution = jobLauncher.run(monthlyAccountInterestJob, new JobParameters(params));
            if (execution.getStatus().getBatchStatus() == COMPLETED) {
                log.info("Monthly interest calculation scheduler has been completed successfully");
            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Exception occurred while running monthly interest calculation scheduler ", e);
        }
    }
}
