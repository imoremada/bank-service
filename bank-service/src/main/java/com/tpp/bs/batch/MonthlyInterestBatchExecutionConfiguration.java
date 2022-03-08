package com.tpp.bs.batch;

import com.tpp.bs.account.AccountService;
import com.tpp.bs.account_adapter.AccountEntity;
import com.tpp.bs.common.DateTimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.Executor;

@Configuration
@EnableBatchProcessing
@ComponentScan("com.tpp.bs")
@Slf4j
public class MonthlyInterestBatchExecutionConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${monthly.batch.execution-chunk-size:20}")
    private int pageSize;

    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public ItemReader<AccountEntity> monthlyAccountEntityReader() {
        JpaPagingItemReader<AccountEntity> reader = new JpaPagingItemReader<>();
        reader.setPageSize(pageSize);
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select a from AccountEntity a");
        return reader;
    }

    @Bean
    public ItemWriter<AccountEntity> monthlyAccountInterestItemWriter(AccountService accountService,
                                                                      Executor executor) {

        return new MonthlyAccountInterestItemWriter(accountService, executor);
    }

    @Bean("monthlyAccountInterestJob")
    public Job monthlyAccountInterestJob(ItemWriter<AccountEntity> monthlyAccountInterestItemWriter,
                                       AccountEntityProcessor entityProcessor) {
        Step step = stepBuilderFactory.get("step")
                .<AccountEntity, AccountEntity>chunk(pageSize)
                .reader(monthlyAccountEntityReader())
                .processor(entityProcessor)
                .writer(monthlyAccountInterestItemWriter)
                .faultTolerant()
                .build();

        return jobBuilderFactory.get("monthlyAccountInterestJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
