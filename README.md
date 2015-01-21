#SleepyBatchlet sample for javaBatch-1.0 on Liberty

SleepyBatchlet is a simple sample batchlet for use with feature javaBatch-1.0 on WebSphere Liberty Profile.
javaBatch-1.0 is Liberty's implementation of the Batch Programming Model in Java EE 7, as specified by JSR 352.

The batchlet itself is rather uninteresting. All it does is sleep in 1 second increments for a default time
of 15 seconds.  The sleep time is configurable via batch property *sleep.time.seconds*.  The batchlet
prints a message to System.out each second, so you can easily verify that it's running.

##Build the sample

For your convenience, the sample application has already been built: SleepyBatchletSample-1.0.war.

To build from source, use maven or import the source into WDT (WebSphere Developer Tools).

##Install and run the sample

1. Use the sample server.xml as a guide for configuring your server with javaBatch-1.0.

2. Configure java batch persistence and create the batch runtime database. See the following knowledge center
article for more information: http://www-01.ibm.com/support/knowledgecenter/was_beta_liberty/com.ibm.websphere.wlp.nd.multiplatform.doc/ae/rwlp_batch_persistence_config.html

3. Install the sample app to your server by copying SleepyBatchletSample-1.0.war into
the server's dropins/ directory.

4. Run the sample. You can kickoff the batch job thru the sample servlet
by hitting the following URL:

        http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=start

##Controlling sample jobs

Besides starting a job, you can also stop, restart, and get status, by hitting the following URLs:

    http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=stop&executionId=xx
    http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=restart&executionId=xx
    http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=xx

Where *executionId=xx* is the job execution ID.  Each http request returns JobInstance and JobExecution
records for the job.  

##Example session

Below is an example session, using cURL.  

Note: The query parameter *sleep.time.seconds* is passed along as a job parameter for the job, and then injected into the batchlet via a batch property.  The property controls how long sleepyBatchlet runs.

```
$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=start'
Job started!
JobInstance: instanceId=1, jobName=sleepy-batchlet
JobExecution: executionId=1, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=2014-09-12 13:36:59.307, startTime=2014-09-12 13:36:59.398, endTime=null, lastUpdatedTime=2014-09-12 13:36:59.398, jobParameters={}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=1'
status
JobInstance: instanceId=1, jobName=sleepy-batchlet
JobExecution: executionId=1, jobName=sleepy-batchlet, batchStatus=COMPLETED, createTime=2014-09-12 13:36:59.307, startTime=2014-09-12 13:36:59.398, endTime=2014-09-12 13:37:14.567, lastUpdatedTime=2014-09-12 13:37:14.567, jobParameters={}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=start&sleep.time.seconds=22'
Job started!
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=null, lastUpdatedTime=2014-09-12 13:41:31.02, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=2'
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=null, lastUpdatedTime=2014-09-12 13:41:31.02, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=stop&executionId=2'
Stop request submitted!
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPING, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=null, lastUpdatedTime=2014-09-12 13:41:41.109, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=2'
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=2014-09-12 13:41:42.041, lastUpdatedTime=2014-09-12 13:41:42.041, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=restart&executionId=2'
Job restarted!
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=3, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=2014-09-12 13:42:05.643, startTime=2014-09-12 13:42:05.646, endTime=null, lastUpdatedTime=2014-09-12 13:42:05.646, jobParameters={}
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=2014-09-12 13:41:42.041, lastUpdatedTime=2014-09-12 13:41:42.041, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=2'
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=3, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=2014-09-12 13:42:05.643, startTime=2014-09-12 13:42:05.646, endTime=null, lastUpdatedTime=2014-09-12 13:42:05.646, jobParameters={}
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=2014-09-12 13:41:42.041, lastUpdatedTime=2014-09-12 13:41:42.041, jobParameters={sleep.time.seconds=22}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/sleepybatchlet?action=status&executionId=2'
status
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=3, jobName=sleepy-batchlet, batchStatus=COMPLETED, createTime=2014-09-12 13:42:05.643, startTime=2014-09-12 13:42:05.646, endTime=2014-09-12 13:42:20.688, lastUpdatedTime=2014-09-12 13:42:20.688, jobParameters={}
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=2014-09-12 13:41:31.011, startTime=2014-09-12 13:41:31.02, endTime=2014-09-12 13:41:42.041, lastUpdatedTime=2014-09-12 13:41:42.041, jobParameters={sleep.time.seconds=22}
```

##Layout

The sample repository contains the following files:

    README.md 
    SleepyBatchletSample-1.0.war - pre-built sample app
    server.xml - sample 
    pom.xml
    src/main/java/com/ibm/ws/jbatch/sample/sleepybatchlet/SleepyBatchlet.java
    src/main/java/com/ibm/ws/jbatch/sample/sleepybatchlet/web/SleepyBatchletServlet.java
    src/main/webapp/WEB-INF/classes/META-INF/batch-jobs/sleepy-batchlet.xml

