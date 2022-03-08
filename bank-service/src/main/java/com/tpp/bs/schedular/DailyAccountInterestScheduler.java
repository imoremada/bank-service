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
import org.springframework.beans.factory.annotation.Value;
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
public class DailyAccountInterestScheduler {
    @Value("${daily.scheduler.chron:0 0 21 * * *}")
    private String dailySchedulerChron;

    private final @Qualifier("dailyAccountInterestJob")  Job dailyAccountInterestJob;

    @Autowired
    private JobRepository jobRepository;

    @Scheduled(cron = "${dailyInterestCronExpression}")//every day at 9pm
    public void dailyInterestCalculationScheduler() {
        log.info("Daily Account Interest Calculation Scheduler has been started");
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        try {
            SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
            try {
                jobLauncher.afterPropertiesSet();
            } catch (Exception e) {
                log.error("Exception occurred when scheduling daily interest calculation scheduler ", e);
            }
            JobExecution execution = jobLauncher.run(dailyAccountInterestJob, new JobParameters(params));
            if (execution.getStatus().getBatchStatus() == COMPLETED) {
                log.info("Daily interest calculation scheduler has been completed successfully");
            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Exception occurred while running daily interest calculation scheduler ", e);
        }
    }
}
