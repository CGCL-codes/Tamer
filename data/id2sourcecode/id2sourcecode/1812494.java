    protected ResourcePool(final int sleepTime, final int poolLimit) {
        super();
        final String thisMethod = s_thisClass + "<init>(" + sleepTime + "): ";
        if (s_log.isOnVerbose()) s_log.write(Log.VERBOSE, thisMethod + "start");
        if (sleepTime > 0) {
            m_sleepTime = sleepTime;
            if (s_log.isOnVerbose()) s_log.write(Log.VERBOSE, thisMethod + "creating tidy up thread...");
            m_tidyUp = new Thread(this);
            if (s_log.isOnVerbose()) s_log.write(Log.VERBOSE, thisMethod + "making tidy up thread a daemon...");
            m_tidyUp.setDaemon(true);
            if (s_log.isOnVerbose()) s_log.write(Log.VERBOSE, thisMethod + "starting tidy up thread...");
            m_tidyUp.start();
        }
        if (poolLimit > 0) {
            m_poolLimit = poolLimit;
        } else {
            m_poolLimit = 0;
        }
        if (s_log.isOnVerbose()) s_log.write(Log.VERBOSE, thisMethod + "end");
    }
