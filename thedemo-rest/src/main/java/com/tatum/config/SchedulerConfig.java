package com.tatum.config;

import com.tatum.quartz.DemoScheduler;
import com.tatum.quartz.job.DemoJob;
import com.tatum.quartz.jobfactory.DemoJobFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.IntStream;

@Configuration
@DependsOn("liquibase")
public class SchedulerConfig {

    @Bean
    public JobFactory jobFactory() {
        return new DemoJobFactory();
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

//    @Bean
//    public SchedulerFactory schedulerFactory(Properties quartzProperties) throws IOException, SchedulerException {
//        SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties);
//        return schedulerFactory;
//    }


    @Bean
    public ArrayList listTriggers(@Value("${demo.quartz.runnerJobs}") int limits
            ,@Value("${demo.quartz.interval}") String cron) {


        ArrayList<Trigger> triggers = new ArrayList<>();

        IntStream.range(0,limits).forEachOrdered( t -> {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("Trigger"+t,"group1")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            triggers.add(trigger);
        });
        return triggers;
    }

    @Bean
    public ArrayList listJobs(@Value("${demo.quartz.runnerJobs}") int limits) {

        ArrayList<JobDetail> jobs = new ArrayList<>();

        IntStream.range(0,limits).forEachOrdered( t -> {
            JobKey jobKey = new JobKey("job" + t, "group1");
            JobDetail jobDetail = JobBuilder.newJob(DemoJob.class)
                    //.usingJobData("myid", "code" + t)
                    .withIdentity(jobKey)
                    .build();
            jobs.add(jobDetail);
        });

        return jobs;
    }

    @Bean
    public DemoScheduler demoScheduler() {
        return new DemoScheduler();
    }

}
