package com.springbatchdemo.processor;

import com.springbatchdemo.entity.student.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Basic;
import javax.persistence.EntityManagerFactory;

@Configuration
public class ProcessorNullJobConfiguration  {

    private static Logger log = LoggerFactory.getLogger(ProcessorNullJobConfiguration.class);

    public static final String JOB_NAME = "processorNullBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    public ProcessorNullJobConfiguration (JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory emf) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.emf = emf;
    }

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "Step")
    public Step step() {
        return stepBuilderFactory.get(BEAN_PREFIX + "Step")
                .<Teacher, Teacher>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Teacher> reader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .name(BEAN_PREFIX + "Reader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("select t from Teacher t")
                .build();
    }

    @Basic
    public ItemProcessor<Teacher, Teacher> processor() {
        return teacher -> {
            boolean isIgnoreTarget = teacher.getId() % 2 == 0L;
            if (isIgnoreTarget) {
                log.info(">>>>>>>>>>>>>>>>>>>>> Teacher name = {}, isIgnoreTarget = {}",
                        teacher.getName(), isIgnoreTarget);
                return null;
            }
            return teacher;
        };
    }

    private ItemWriter<Teacher> writer() {
        return items -> {
            for (Teacher item : items) {
                log.info("Teacher Name = {}", item.getName());
            }
        };
    }

}
