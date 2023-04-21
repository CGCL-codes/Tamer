    @Override
    public void run() {
        final String thisMethod = s_thisClass + "run: ";
        final boolean logVerbose = s_log.isOnVerbose();
        if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "start");
        Runnable task = null;
        try {
            while (m_running) {
                while (m_running && task == null) {
                    if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "waiting for task.");
                    try {
                        synchronized (this) {
                            wait(m_sleepTime);
                        }
                    } catch (InterruptedException e) {
                        s_log.warn(thisMethod + "wait interrupted: " + e);
                    }
                    if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "stopped waiting for task.");
                    task = nextTask();
                    if (logVerbose && task != null) s_log.write(Log.VERBOSE, thisMethod + "task is: " + task);
                }
                if (task != null) {
                    if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "running task...");
                    try {
                        task.run();
                    } catch (Exception e) {
                        s_log.error(thisMethod + e, e);
                        if (s_log.isOnDebug()) s_log.write(Log.DEBUG, thisMethod + "ah, ah, ah, ah, staying alive");
                    } catch (Error e) {
                        s_log.error(thisMethod + "re-throwing " + e, e);
                        throw e;
                    }
                    synchronized (this) {
                        if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "task over - calling notifyAll on " + this);
                        notifyAll();
                        task = nextTask();
                        if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "next task: " + task);
                        if (task == null) {
                            if (logVerbose) s_log.write(Log.VERBOSE, thisMethod + "freeing thread...");
                            m_state = FREE;
                            try {
                                m_threadPool.freeElement(this);
                            } catch (ResourcePoolException e) {
                                s_log.warn(thisMethod + "freeing this thread in pool: " + e);
                            }
                        }
                    }
                }
            }
        } finally {
            synchronized (this) {
                m_state = CLOSED;
            }
        }
        if (s_log.isOnTrace()) s_log.write(Log.TRACE, thisMethod + "end");
    }
