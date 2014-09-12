#SleepyBatchlet sample for javaBatch-1.0 on Liberty

SleepyBatchlet is a simple sample batchlet for use with feature javaBatch-1.0 on WebSphere Liberty Profile.
javaBatch-1.0 is Liberty's implementation of the Batch Programming Model in Java EE 7, as specified by JSR 352.

The batchlet itself is rather uninteresting. All it does is sleep in 1 second increments for a default time
of 15 seconds.  The sleep time is configurable via batch property *sleep.time.seconds*.  The batchlet
prints a message to System.out each second, so you can easily verify that it's running.

##Install and run the sample

1. From your wlp/ installation directory, run the self-extracting sample archive:

        $ java -jar LibertyServer.jar

2. Start the server:

        $ bin/server start LibertyServer

3. Run the sample. You can kickoff the batch job thru the sample servlet
by hitting the following URL, either via a browser or cURL or your preferred HTTP client:

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

Note: The query parameter *sleep.time.seconds* is passed along as a batch property for the job.
The property controls how long sleepyBatchlet runs.

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

##Notes

* The self-extracting archive **LibertyServer.jar** contains a pre-configured liberty server (named LibertyServer) with the
SleepyBatchlet sample application already installed.  It also contains a pre-built Derby database for
the Batch runtime tables, which are needed by javaBatch-1.0 for managing jobs.  Please see the Liberty
Knowledge Center for information on how to create, configure, and customize the batch database.

* The sample application source code is included. It can be built via maven or imported into WDT (WebSphere Developer Tools).

##Layout

The sample repository contains the following files:

    LibertyServer.jar
    README.md
    pom.xml
    src/main/java/com/ibm/ws/jbatch/sample/sleepybatchlet/SleepyBatchlet.java
    src/main/java/com/ibm/ws/jbatch/sample/sleepybatchlet/web/SleepyBatchletServlet.java
    src/main/webapp/WEB-INF/classes/META-INF/batch-jobs/sleepy-batchlet.xml

The self-extracting archive **LibertyServer.jar** contains:

    wlp/usr/servers/LibertyServer/server.xml
    wlp/usr/servers/LibertyServer/dropins/SleepyBatchletSample-1.0.war
    wlp/usr/servers/LibertyServer/resources/BATCHDB/
    wlp/usr/servers/LibertyServer/resources/derby/derby.jar

...along with several data files under `BATCHDB` and classes that handle self-extraction of the jar.
These files have been omitted from the above list, for brevity and clarity.

