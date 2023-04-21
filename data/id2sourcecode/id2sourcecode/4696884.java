    public WorkerThreadPool(AtomConsumer oAtomConsumer, Properties oEnvironmentProps) {
        int nThreads = Integer.parseInt(oEnvironmentProps.getProperty("maxschedulerthreads", "1"));
        if (DebugFile.trace) DebugFile.writeln("maxschedulerthreads=" + String.valueOf(nThreads));
        oEnvProps = oEnvironmentProps;
        aThreads = new WorkerThread[nThreads];
        aStartTime = new long[nThreads];
        for (int t = 0; t < nThreads; t++) {
            if (DebugFile.trace) DebugFile.writeln("new WorkerThread(" + String.valueOf(t) + ")");
            aThreads[t] = new WorkerThread(this, oAtomConsumer);
            aThreads[t].setName("WorkerThread_" + String.valueOf(t));
        }
    }
