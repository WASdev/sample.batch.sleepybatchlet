# SleepyBatchlet sample for batch-1.0 on Liberty

SleepyBatchlet is a simple sample batchlet for use with feature batch-1.0 on WebSphere Liberty Profile.
batch-1.0 is Liberty's implementation of the Batch Programming Model in Java EE 7, as specified by JSR 352.

The batchlet itself is rather uninteresting. All it does is sleep in 1 second increments for a default time
of 15 seconds.  The sleep time is configurable via batch property *sleep.time.seconds*.  The batchlet
prints a message to System.out each second, so you can easily verify that it's running.

## Build the sample

For your convenience, the sample application has already been built: SleepyBatchletSample-1.0.war.

To build from source, use maven or import the project into WDT (WebSphere Developer Tools).

## Install and run the sample

1. Use the sample server.xml as a guide for configuring your Liberty server with batch-1.0.

2. Install the sample app to your server by copying SleepyBatchletSample-1.0.war into
the server's dropins/ directory.

3. Run the sample. The sample app includes a generic JobOperatorServlet that acts as
thin wrapper around the JobOperator API.  You can start the batch job thru this servlet
by hitting the following URL:

    http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=start&jobXMLName=sleepy-batchlet

## Controlling sample jobs

Besides starting a job, you can use the JobOperatorServlet to stop, restart, and get status, by hitting the following URLs:

    http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=stop&executionId=xx
    http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=restart&executionId=xx
    http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=status&executionId=xx

Where *executionId=xx* is the job execution ID.  Each http request returns JobInstance and JobExecution
records for the job.  

For a complete list of actions available from the JobOperatorServlet, use action=help:

    http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=help

**Note:** The JobOperatorServlet is a generic HTTP wrapper for the JobOperator API.  You can copy it
into your own application and use it to control the jobs for that application.  


## Example session

Below is an example session, using cURL.  

Note: The query parameter *sleep.time.seconds* is passed along as a job parameter for the job, and then injected into 
the batchlet via a batch property.  The property controls how long SleepyBatchlet runs.

```
$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=start&jobXMLName=sleepy-batchlet'
start(jobXMLName=sleepy-batchlet, jobParameters=null): Job started!
JobInstance: instanceId=1, jobName=sleepy-batchlet
JobExecution: executionId=1, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=Fri May 22 11:24:00 EDT 2015, startTime=Fri May 22 11:24:00 EDT 2015, endTime=null, lastUpdatedTime=Fri May 22 11:24:00 EDT 2015, jobParameters=null

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=status&executionId=1'
status(executionId=1):
JobInstance: instanceId=1, jobName=sleepy-batchlet
JobExecution: executionId=1, jobName=sleepy-batchlet, batchStatus=COMPLETED, createTime=Fri May 22 11:24:00 EDT 2015, startTime=Fri May 22 11:24:00 EDT 2015, endTime=Fri May 22 11:24:15 EDT 2015, lastUpdatedTime=Fri May 22 11:24:15 EDT 2015, jobParameters=null

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=start&jobXMLName=sleepy-batchlet&jobParameters=sleep.time.seconds=32'
start(jobXMLName=sleepy-batchlet, jobParameters={sleep.time.seconds=32}): Job started!
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STARTING, createTime=Fri May 22 11:25:11 EDT 2015, startTime=null, endTime=null, lastUpdatedTime=Fri May 22 11:25:11 EDT 2015, jobParameters={sleep.time.seconds=32}

$  curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=status&executionId=2'
status(executionId=2):
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=Fri May 22 11:25:11 EDT 2015, startTime=Fri May 22 11:25:11 EDT 2015, endTime=null, lastUpdatedTime=Fri May 22 11:25:11 EDT 2015, jobParameters={sleep.time.seconds=32}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=stop&executionId=2'
stop(executionId=2): Stop request submitted!
status(executionId=2):
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPING, createTime=Fri May 22 11:25:11 EDT 2015, startTime=Fri May 22 11:25:11 EDT 2015, endTime=null, lastUpdatedTime=Fri May 22 11:25:31 EDT 2015, jobParameters={sleep.time.seconds=32}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=status&executionId=2'
status(executionId=2):
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=Fri May 22 11:25:11 EDT 2015, startTime=Fri May 22 11:25:11 EDT 2015, endTime=Fri May 22 11:25:31 EDT 2015, lastUpdatedTime=Fri May 22 11:25:31 EDT 2015, jobParameters={sleep.time.seconds=32}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=restart&executionId=2&restartParameters=sleep.time.seconds=10'
restart(executionId=2, restartParameters={sleep.time.seconds=10}): Job restarted!
status(executionId=2):
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=3, jobName=sleepy-batchlet, batchStatus=STARTED, createTime=Fri May 22 11:26:29 EDT 2015, startTime=Fri May 22 11:26:29 EDT 2015, endTime=null, lastUpdatedTime=Fri May 22 11:26:29 EDT 2015, jobParameters={sleep.time.seconds=10}
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=Fri May 22 11:25:11 EDT 2015, startTime=Fri May 22 11:25:11 EDT 2015, endTime=Fri May 22 11:25:31 EDT 2015, lastUpdatedTime=Fri May 22 11:25:31 EDT 2015, jobParameters={sleep.time.seconds=32}

$ curl 'http://localhost:9080/SleepyBatchletSample-1.0/joboperator?action=status&executionId=2'
status(executionId=2):
JobInstance: instanceId=2, jobName=sleepy-batchlet
JobExecution: executionId=3, jobName=sleepy-batchlet, batchStatus=COMPLETED, createTime=Fri May 22 11:26:29 EDT 2015, startTime=Fri May 22 11:26:29 EDT 2015, endTime=Fri May 22 11:26:39 EDT 2015, lastUpdatedTime=Fri May 22 11:26:39 EDT 2015, jobParameters={sleep.time.seconds=10}
JobExecution: executionId=2, jobName=sleepy-batchlet, batchStatus=STOPPED, createTime=Fri May 22 11:25:11 EDT 2015, startTime=Fri May 22 11:25:11 EDT 2015, endTime=Fri May 22 11:25:31 EDT 2015, lastUpdatedTime=Fri May 22 11:25:31 EDT 2015, jobParameters={sleep.time.seconds=32}
```
