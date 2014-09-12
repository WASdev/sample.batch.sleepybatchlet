package com.ibm.ws.jbatch.sample.sleepybatchlet.web;

import java.util.logging.Logger;
import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is a simple wrapper around the SleepyBatchlet job.
 *
 * It uses the BatchRuntime JobOperator to start, stop, restart,
 * and get status of jobs.
 */
@WebServlet(urlPatterns = { "/sleepybatchlet" })
public class SleepyBatchletServlet extends HttpServlet {

    protected final static Logger logger = Logger.getLogger(SleepyBatchletServlet.class.getName());

    /**
     * Logging helper.
     */
    protected static void log(String method, Object msg) {
        System.out.println("SleepyBatchletServlet: " + method + ": " + String.valueOf(msg));
        // logger.info("SleepyBatchletServlet: " + method + ": " + String.valueOf(msg));
    }

    /**
     * Request entry point.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log("doGet", "URL: " + request.getRequestURL() + "?" + request.getQueryString());

        String action = request.getParameter("action");

        if ("start".equalsIgnoreCase(action)) {
            start(request, response);
        } else if ("status".equalsIgnoreCase(action)) {
            status(request, response);
        } else if ("stop".equalsIgnoreCase(action)) {
            stop(request, response);
        } else if ("restart".equalsIgnoreCase(action)) {
            restart(request, response);
        } else {
            throw new IOException("action not recognized: " + action );
        }
    }

    /**
     * Start the job.
     */
    protected void start(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long execId = jobOperator.start("sleepy-batchlet", getJobParameters(request, "sleep.time.seconds"));

        JobInstance jobInstance = jobOperator.getJobInstance(execId);
        JobExecution jobExecution = jobOperator.getJobExecution(execId);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println( "Job started!" );
        response.getWriter().println( "JobInstance: " + jobInstanceToString(jobInstance));
        response.getWriter().println( "JobExecution: " + jobExecutionToString(jobExecution) );
    }


    /**
     * Get status for the job identified by the 'executionId' query parm.
     */
    protected void status(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long execId = getLongParm(request, "executionId");

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobInstance jobInstance = jobOperator.getJobInstance(execId);
        List<JobExecution> jobExecutions = jobOperator.getJobExecutions(jobInstance);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println( "status");
        response.getWriter().println( "JobInstance: " + jobInstanceToString(jobInstance));
        for (JobExecution jobExecution : jobExecutions) {
            response.getWriter().println( "JobExecution: " + jobExecutionToString(jobExecution) );
        }
    }

    /**
     * Stop the job identified by the 'executionId' query parm.
     */
    protected void stop(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long execId = getLongParm(request, "executionId");

        BatchRuntime.getJobOperator().stop(execId);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println( "Stop request submitted!" );

        status(request, response);
    }

    /**
     * Restart the job identified by the 'executionId' query parm.
     */
    protected void restart(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long execId = getLongParm(request, "executionId");

        long newExecId = BatchRuntime.getJobOperator().restart(execId, getJobParameters(request, "sleep.time.seconds"));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println( "Job restarted!" );

        status(request, response);
    }

    /**
     * @return the value for the given query parm as a long.
     *
     * @throws IOException if the parm is not defined.
     */
    protected long getLongParm(HttpServletRequest request, String parmName) throws IOException {

        String parmVal = request.getParameter(parmName);
        if (parmVal == null || parmVal.isEmpty()) {
            throw new IOException("Must specify parameter '" + parmName + "' in the query string");
        }

        return Long.parseLong(parmVal);
    }

    /**
     * Parse job parameters from the request's query parms.
     *
     * @param queryParmNames The query parms to include in the job parameters Properties object
     *
     * @return the given query parms as a Properties object.
     */
    protected Properties getJobParameters(HttpServletRequest request, String... queryParmNames) throws IOException {
        Properties retMe = new Properties();

        for (String queryParmName : queryParmNames) {
            String queryParmValue = request.getParameter(queryParmName);
            if (queryParmValue != null) {
                retMe.setProperty( queryParmName, queryParmValue );
            }
        }

        return retMe;
    }

    /**
     * @return a stringified version of the jobInstance record.
     */
    protected String jobInstanceToString(JobInstance jobInstance) {
        return "instanceId=" + jobInstance.getInstanceId() 
                + ", jobName=" + jobInstance.getJobName();
    }

    /**
     * @return a stringified version of the jobExecution record.
     */
    protected String jobExecutionToString(JobExecution jobExecution) {
        return "executionId=" + jobExecution.getExecutionId() 
                + ", jobName=" + jobExecution.getJobName()
                + ", batchStatus=" + jobExecution.getBatchStatus()
                + ", createTime=" + jobExecution.getCreateTime()
                + ", startTime=" + jobExecution.getStartTime()
                + ", endTime=" + jobExecution.getEndTime()
                + ", lastUpdatedTime=" + jobExecution.getLastUpdatedTime()
                + ", jobParameters=" + jobExecution.getJobParameters();
    }
    
}
