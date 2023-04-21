    public void execute() {
        if (SC_SIZE.equals(subCommand)) {
            int cacheSize = CacheBO.getInstance().getCacheSize();
            CommandLineProcessor.getInstance().write("Cache contains " + cacheSize + " entryes");
        } else if (THREADS.equals(subCommand)) {
            CommandLineProcessor.getInstance().write("The following application threads are alive:");
            Map<Thread, StackTraceElement[]> threadMaps = Thread.getAllStackTraces();
            for (Thread thread : threadMaps.keySet()) {
                if (!thread.isDaemon() && thread.isAlive()) {
                    CommandLineProcessor.getInstance().write("   " + thread.getName() + " [" + thread.getState() + "]");
                }
            }
        } else if (SC_LAST_N.equals(subCommand)) {
            listLastCommand();
        }
    }
