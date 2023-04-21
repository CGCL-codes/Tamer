    public ThreadState getThreadState(PySystemState newSystemState) {
        Thread t = Thread.currentThread();
        ThreadState ts = cachedThreadState;
        if (ts != null && ts.thread == t) {
            return ts;
        }
        if (threads == null) {
            threads = new java.util.Hashtable();
        }
        ts = (ThreadState) threads.get(t);
        if (ts == null) {
            if (newSystemState == null) {
                Py.writeDebug("threadstate", "no current system state");
                newSystemState = Py.defaultSystemState;
            }
            ts = new ThreadState(t, newSystemState);
            threads.put(t, ts);
            additionCounter++;
            if (additionCounter > MAX_ADDITIONS) {
                cleanupThreadTable();
                additionCounter = 0;
            }
        }
        cachedThreadState = ts;
        return ts;
    }
