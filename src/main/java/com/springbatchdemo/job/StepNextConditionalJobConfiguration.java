package com.springbatchdemo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private Logger log = LoggerFactory.getLogger(StepNextConditionalJobConfiguration.class);

    public StepNextConditionalJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // conditionalJobStep1()이 FAILED 일 경우
                    .to(conditionalJobStep3()) // conditionalJobStep3() 로 이동
                    .on("*") // conditionalJobStep3()의 결과에 상관없이
                    .end() // conditionalJobStep3()으로 이동하면 Flow가 종료
                .from(conditionalJobStep1()) //conditionalJobStep1()으로 부터
                    .on("*") // FAILED 외의 모든 경우
                    .to(conditionalJobStep2()) // conditionalJobStep2()로 이동
                    .next(conditionalJobStep3()) // conditionalJobStep2()가 정상 종료되고 나서 conditionalJobStep3()로 이동
                    .on("*") // conditionalJobStep2()의 결과에 상관없이
                    .end() // conditionalJobStep2()로 이동하면 Flow가 종료
                .end() // Job 종료
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("conditionalJobStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>>>>>>>>> This is StepNextConditionalJob Step1");

                    /**
                     * ExitStatus를 FAILED로 지정
                     * 해당 status를 보고 Flow가 진행
                     */
                    contribution.setExitStatus(ExitStatus.FAILED);

                    return  RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>>>>>>>>> This is StepNextConditionalJob Step2");

                    return  RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>>>>>>>>> This is StepNextConditionalJob Step3");

                    return  RepeatStatus.FINISHED;
                }))
                .build();
    }
}
