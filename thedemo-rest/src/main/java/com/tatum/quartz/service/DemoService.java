package com.tatum.quartz.service;

import java.util.Date;

public class DemoService {

    private String myid;

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public void run() {
        System.out.println("This is myid:" + myid );
    }
}
