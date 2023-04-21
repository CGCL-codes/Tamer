    public synchronized boolean runningJob(JobRuntime pData, long pWaiting, JobConfig pJc, String pJproxy) {
        if (!mgIndex.hasMoreElements()) {
            mgIndex = mg.keys();
        }
        RunnerManagerThread ttemp = mgIndex.nextElement();
        Hashtable<String, Runner> tlist = mg.get(ttemp);
        HashMap desc = new HashMap();
        try {
            String tmp = UUID.randomUUID().toString();
            while (tlist.get(tmp) != null) {
                tmp = UUID.randomUUID().toString();
            }
            tlist.put(tmp, new Runner(pData, desc, pWaiting, pJc, pJproxy, tmp));
            decProgressJobs();
            writeServiceLogg("jobsubmit thread=\"" + ttemp.getName() + "\" poolsize=\"" + getRunnerSize() + "\" freesize=\"" + getFreeRunnerCount() + "\" threadpool=\"" + tlist.size() + "\"", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
