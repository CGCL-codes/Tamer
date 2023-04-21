    @Test
    public void jobExplorerWithSuccess() throws Exception {
        launchSuccessJob();
        List<JobInstance> jobInstances = jobExplorer.getJobInstances("importProductsJobSuccess", 0, 30);
        Assert.assertEquals(1, jobInstances.size());
        JobInstance jobInstance = jobInstances.get(0);
        Assert.assertEquals("importProductsJobSuccess", jobInstance.getJobName());
        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
        Assert.assertEquals(1, jobExecutions.size());
        JobExecution jobExecution = jobExecutions.get(0);
        Assert.assertEquals("exitCode=COMPLETED;exitDescription=", jobExecution.getExitStatus().toString());
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        Assert.assertEquals(1, stepExecutions.size());
        StepExecution stepExecution = stepExecutions.iterator().next();
        Assert.assertEquals("readWriteSuccess", stepExecution.getStepName());
        Assert.assertEquals("StepExecution: id=1, version=3, name=readWriteSuccess, status=COMPLETED," + " exitStatus=COMPLETED, readCount=8, filterCount=0, writeCount=8 readSkipCount=0," + " writeSkipCount=0, processSkipCount=0, commitCount=1, rollbackCount=0", stepExecution.getSummary());
        Assert.assertEquals(8, stepExecution.getReadCount());
        Assert.assertEquals(8, stepExecution.getWriteCount());
        Assert.assertEquals(0, stepExecution.getFilterCount());
        Assert.assertEquals(0, stepExecution.getReadSkipCount());
        Assert.assertEquals(0, stepExecution.getWriteSkipCount());
        Assert.assertEquals(0, stepExecution.getProcessSkipCount());
        Assert.assertEquals(1, stepExecution.getCommitCount());
        Assert.assertEquals(0, stepExecution.getRollbackCount());
    }
