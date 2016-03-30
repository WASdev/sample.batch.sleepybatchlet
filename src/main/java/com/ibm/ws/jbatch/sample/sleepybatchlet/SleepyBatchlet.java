/*
 * COPYRIGHT LICENSE: This information contains sample code provided in source
 * code form. You may copy, modify, and distribute these sample programs in any 
 * form without payment to IBM for the purposes of developing, using, marketing 
 * or distributing application programs conforming to the application programming 
 * interface for the operating platform for which the sample code is written. 
 * 
 * Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE 
 * ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, 
 * BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, 
 * SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR 
 * CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
 * OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE
 * MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE
 * SOURCE CODE.
 * 
 * (C) Copyright IBM Corp. 2014.
 * 
 * All Rights Reserved. Licensed Materials - Property of IBM.  
 */

package com.ibm.ws.jbatch.sample.sleepybatchlet;

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;

/**
 * This simple batchlet sleeps in 1 second increments up to "sleep.time.seconds".
 * sleep.time.seconds can be configured as a batch property.  If not configured,
 * the default is 15 seconds.
 *
 */
public class SleepyBatchlet extends AbstractBatchlet {

    private final static Logger logger = Logger.getLogger(SleepyBatchlet.class.getName());

    /**
     * Logging helper.
     */
    protected static void log(String method, Object msg) {
        System.out.println("SleepyBatchlet: " + method + ": " + String.valueOf(msg));
        // logger.info("SleepyBatchlet: " + method + ": " + String.valueOf(msg));
    }

    /**
     * This flag gets set if the batchlet is stopped.  This will break the batchlet
     * out of its sleepy loop.
     */
    private volatile boolean stopRequested = false;

    /**
     * The total sleep time, in seconds.  
     */
    @Inject
    @BatchProperty(name = "sleep.time.seconds")
    String sleepTimeSecondsProperty;
    private int sleepTime_s = 15; 

    /**
     * Main entry point.
     */
    @Override
    public String process() throws Exception {

        log("process", "entry");

        if (sleepTimeSecondsProperty != null) {
            sleepTime_s = Integer.parseInt(sleepTimeSecondsProperty);
        }
        
        log("process", "sleep for: " + sleepTime_s );

        int i;
        for (i = 0; i < sleepTime_s && !stopRequested; ++i) {
            log("process", "[" + i + "] sleeping for a second...");
            Thread.sleep(1 * 1000);
        }

        String exitStatus = "SleepyBatchlet:i=" + i + ";stopRequested=" + stopRequested;
        log("process", "exit. exitStatus: " + exitStatus);

        return exitStatus;
    }

    /**
     * Called if the batchlet is stopped by the container.
     */
    @Override
    public void stop() throws Exception {
        log("stop:", "");
        stopRequested = true;
    }

}

