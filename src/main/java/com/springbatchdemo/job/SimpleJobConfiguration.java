package com.springbatchdemo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// lombok이 안됨 이유를 찾아야함
//@Slf4j // log 사용을 위한 lombok 어노테이션
//@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration // Spring Batch의 모든 Job은 @configuration으로 등록해서 사용
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    private static Logger log = LoggerFactory.getLogger(SimpleJobConfiguration.class);

    public SimpleJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    // Spring Batch 에서 Job 은 하나의 배치 작업 단위를 이야기함
    // Job 안에는 아래 형식 처럼 1개 이상의 Step 이 존재하고,
    // Step 안에 Tasklet or Reader / Processor / Writer 묶음이 존재
    // Tasklet은 Spring MVC의 @Component, @Bean과 비슷한 역활이라 봐도 됨
    // 명확한 역활은 없지만, 개발자가 지정한 커스텀한 기능을 위한 단위라고 보면됨
    @Bean
    public Job simpleJob() {
        // simpleJob 이라는 이름의 Batch Job 을 생성
        // job 의 이름은 별도로 지정하지 않고, Builder 를 통해 지정
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        // simpleStep1 이라는 이름의 Batch Job 을 생성
        // jobBuilderFactory.get("simpleJob") 와 마찬가지로 Builder 를 통해 이름 지정
        return stepBuilderFactory.get("simpleStep1")
                // Step 안에서 수행될 기능을 명시
                // Tasklet 은 'Step 안에서 단일로 수행될 커스텀한 기능'들을 선언할때 사용됨
                // Batch가 수행되면 log.info 가 출력됨됨
               .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
