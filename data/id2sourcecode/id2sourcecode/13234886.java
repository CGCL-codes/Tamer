    synchronized String dumpAppState() {
        StackTraceElement[] dump;
        StringWriter msg = new StringWriter();
        msg.write(new Date().toString() + ": SJQ Application Dump\n");
        if (qLoaderThread != null && qLoaderThread.isAlive()) {
            msg.write("\tMediaQueueLoader thread is alive...\n");
            dump = qLoaderThread.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else msg.write("\tMediaQueueLoader thread is dead!\n");
        if (sysMsgQ != null && sysMsgThread.isAlive()) {
            msg.write("\tSysMsgQueueLoader thread is alive...\n");
            dump = sysMsgThread.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else msg.write("\tSysMsgQueueLoader thread is dead!\n");
        if (cMonThread != null && cMonThread.isAlive()) {
            msg.write("\tClientMonitor thread is alive...\n");
            dump = cMonThread.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else msg.write("\tClientMonitor thread is dead!\n");
        if (clntThread != null && clntThread.isAlive()) {
            msg.write("\tInteralTaskClient thread is alive...\n");
            dump = clntThread.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else msg.write("\tInternalTaskClient thread is dead!\n");
        if (activeLogCleaner != null && activeLogCleaner.isAlive()) {
            msg.write("\tA LogCleaner thread is currently active...\n");
            dump = activeLogCleaner.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else if (activeLogCleaner != null) msg.write("\tMost recent LogCleaner thread is dead (this is normal behaviour)!\n"); else msg.write("\tNo LogCleaner thread has ever been created (this is ABNORMAL)!\n");
        if (activeFileCleaner != null && activeFileCleaner.isAlive()) {
            msg.write("\tA FileCleaner thread is currently active...\n");
            dump = activeFileCleaner.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else if (activeFileCleaner != null) msg.write("\tMost recent FileCleaner thread is dead (this is normal behaviour)!\n"); else msg.write("\tNo FileCleaner thread has ever been created (this is ABNORMAL)!\n");
        if (activeVacuumCleaner != null && activeVacuumCleaner.isAlive()) {
            msg.write("\tA VacuumCleaner thread is currently active...\n");
            dump = activeVacuumCleaner.getStackTrace();
            for (StackTraceElement s : dump) msg.write("\t\t" + s.toString() + "\n");
        } else if (activeVacuumCleaner != null) msg.write("\tMost recent VacuumCleaner thread is dead (this is normal behaviour)!\n"); else msg.write("\tNo VacuumCleaner thread has ever been created (this is ABNORMAL)!\n");
        LOG.warn(msg.toString());
        return msg.toString();
    }
