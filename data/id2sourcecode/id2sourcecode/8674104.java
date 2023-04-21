    private void executeJob(String workingDir, String cmd) throws JobManagerException {
        try {
            if (props.getProperty("fork.jobs.limit") != null) {
                long jobLimit = Integer.parseInt(props.getProperty("fork.jobs.limit"));
                while (true) {
                    long runningJobs;
                    try {
                        runningJobs = HibernateUtil.getNumExecutingJobs();
                        logger.debug("Number of running jobs: " + runningJobs);
                    } catch (Exception e) {
                        String msg = "Exception while retrieving number of jobs from database: " + e.getMessage();
                        logger.error(msg);
                        throw new JobManagerException(msg);
                    }
                    if (runningJobs >= jobLimit) {
                        try {
                            logger.debug("Waiting for number of Fork jobs to fall below limit");
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            logger.warn(e);
                        }
                    } else {
                        break;
                    }
                }
            }
            logger.debug("Working directory: " + workingDir);
            proc = Runtime.getRuntime().exec(cmd, null, new File(workingDir));
            stdoutThread = writeStdOut(proc, workingDir);
            stderrThread = writeStdErr(proc, workingDir);
        } catch (IOException ioe) {
            String msg = "Error while running executable via fork - " + ioe.getMessage();
            logger.error(msg);
            throw new JobManagerException(msg);
        }
        status.setCode(GramJob.STATUS_ACTIVE);
        status.setMessage("Execution in progress");
        started = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
