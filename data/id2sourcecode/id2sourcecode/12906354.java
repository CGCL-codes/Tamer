    public void runTask(final Runnable task) throws IllegalThreadStateException {
        final String thisMethod = s_thisClass + "runTask: ";
        final boolean logVerbose = s_log.isOnVerbose();
        if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "start");
        if (task == null) {
            throw new NullPointerException(thisMethod + "task is null");
        }
        synchronized (this) {
            if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "checking state...");
            if (m_state != ALLOCATED) {
                if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "thread not allocated - throwing IllegalThreadStateException...");
                throw new IllegalThreadStateException("Thread not ALLOCATED");
            }
            if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "adding task (" + task + ")...");
            m_tasks.add(task);
            if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "setting state...");
            m_state = QUEUEING;
            notify();
        }
        if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "end");
    }
