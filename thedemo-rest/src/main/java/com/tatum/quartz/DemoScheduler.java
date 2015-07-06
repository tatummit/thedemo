package com.tatum.quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.IntStream;

public class DemoScheduler implements InitializingBean{
    private Scheduler scheduler;

    @Autowired
    public void setScheduler(Properties quartzProperties,@Qualifier("listTriggers")ArrayList<Trigger>  listTriggers
            ,@Qualifier("listJobs")ArrayList<JobDetail> listJobs) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties);
        scheduler = schedulerFactory.getScheduler();
        IntStream.range(0,listTriggers.size() -1).forEachOrdered(t -> {
            try {
                scheduler.scheduleJob(listJobs.get(t), listTriggers.get(t));
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler.start();
    }
}
