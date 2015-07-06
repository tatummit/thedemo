package com.tatum.quartz.jobfactory;

import com.tatum.exception.DemoIllegalParameterException;
import com.tatum.quartz.job.DemoJob;
import com.tatum.quartz.service.DemoService;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

public class DemoJobFactory implements JobFactory {

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        try {
            Object job = createJobInstance(triggerFiredBundle);
            return this.adaptJob(job);
        } catch (Exception e) {
            throw new DemoIllegalParameterException("Cannot create job", e);
        }
    }

    protected Job adaptJob(Object jobObject) throws Exception {
        if(jobObject instanceof Job) {
            return (Job)jobObject;
        } else {
            throw new IllegalArgumentException("Unable to execute job class [" + jobObject.getClass().getName() + "]: only [org.quartz.Job] and [java.lang.Runnable] supported.");
        }
    }

    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        JobDetail jobDetail = bundle.getJobDetail();
        if(jobDetail.getJobClass().equals(DemoJob.class) ) {
            DemoJob demoJob = (DemoJob) jobDetail.getJobClass().newInstance();
            DemoService service = new DemoService();
            String myid = (String) jobDetail.getJobDataMap().get("myid");
            service.setMyid(myid);
            demoJob.setDemoService(service);
            return demoJob;
        }else {
            return jobDetail.getJobClass().newInstance();
        }
    }
}
