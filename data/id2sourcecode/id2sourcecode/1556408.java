    public String launchJob(String argList, Integer numProcs, String workingDir) throws JobManagerException {
        logger.info("called");
        wd = workingDir;
        if (config == null) {
            String msg = "Can't find application configuration - " + "Plugin not initialized correctly";
            logger.error(msg);
            throw new JobManagerException(msg);
        }
        String args = config.getDefaultArgs();
        if (args == null) {
            args = argList;
        } else {
            String userArgs = argList;
            if (userArgs != null) args += " " + userArgs;
        }
        if (args != null) {
            args = args.trim();
        }
        logger.debug("Argument list: " + args);
        String systemProcsString = props.getProperty("num.procs");
        int systemProcs = 0;
        if (systemProcsString != null) {
            systemProcs = Integer.parseInt(systemProcsString);
        }
        String cmd = null;
        String rsl = new String();
        if ((args != null) && (!(args.equals("")))) {
            logger.debug("Appending arguments: " + args);
        }
        logger.debug("CMD: " + cmd);
        String localIP = null;
        String[] wd_tokens = workingDir.split("/");
        String jobID = wd_tokens[wd_tokens.length - 1];
        String csf4WD = props.getProperty("csf4.workingDir");
        if (csf4WD == null) {
            logger.fatal("Can't find property: csf4.workingDir");
        }
        String remoteDir = csf4WD + "/" + jobID;
        try {
            localIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.error("Can't figure out IP address for localhost: " + e.getMessage());
            logger.error("Can't figure out IP address for localhost: " + e.getMessage());
        }
        String fullBinLocation = config.getBinaryLocation();
        int index = fullBinLocation.indexOf(":");
        String appName = null;
        String binLocation = null;
        if (index > 0) {
            appName = fullBinLocation.substring(0, index);
            binLocation = fullBinLocation.substring(index + 1, fullBinLocation.length());
        } else {
            binLocation = fullBinLocation;
        }
        rsl = "&";
        if (appName != null) {
            rsl += "(application=" + appName + ")";
        }
        rsl += "(executable=" + binLocation + ")" + "(directory=opal_runs/" + jobID + ")" + "(stdout=csf_stdout.txt)" + "(stderr=csf_stderr.txt)" + "(stagein=\"" + workingDir + "->opal_runs/" + jobID + "/\")" + "(stageout=\"" + remoteDir + "/->" + workingDir + "\")";
        if (config.isParallel()) {
            if (numProcs == null) {
                String msg = "Number of processes unspecified for parallel job";
                logger.error(msg);
                throw new JobManagerException(msg);
            } else if (numProcs.intValue() > systemProcs) {
                String msg = "Processors required - " + numProcs + ", available - " + systemProcs;
                logger.error(msg);
                throw new JobManagerException(msg);
            }
            rsl += "(jobtype=\"mpi\")" + "(count=" + numProcs + ")";
        }
        if (args != null) {
            args = "\"" + args + "\"";
            args = args.replaceAll("[\\s]+", "\" \"");
            rsl += "(arguments=" + args + ")";
        }
        logger.debug("RSL: " + rsl);
        BufferedWriter out;
        String read;
        String rslfile = new String(workingDir + "/job.rsl");
        try {
            out = new BufferedWriter(new FileWriter(rslfile));
            out.write(rsl);
            out.close();
        } catch (IOException e) {
            System.out.println("There was a problem:" + e);
        }
        cmd = "csf-job-create" + " " + "-r" + " " + rslfile + " " + "-sub";
        try {
            logger.debug("Working directory: " + workingDir);
            proc_submit_job = Runtime.getRuntime().exec(cmd, envp, new File(workingDir));
            stdoutThread = writeStdOut(proc_submit_job, workingDir, "stdout.txt");
            stderrThread = writeStdErr(proc_submit_job, workingDir, "stderr.txt");
        } catch (IOException ioe) {
            String msg = "Error while running executable via fork - " + ioe.getMessage();
            logger.error(msg);
            throw new JobManagerException(msg);
        }
        status.setCode(GramJob.STATUS_PENDING);
        status.setMessage("Execution in progress");
        System.out.println("CSF Job Submitting.");
        started = true;
        synchronized (this) {
            this.notifyAll();
        }
        return proc_submit_job.toString();
    }
