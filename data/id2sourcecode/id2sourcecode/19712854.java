    public static void saveLogs(String filename) throws IOException {
        FileHandler logFile = new FileHandler(filename, FileHandler.HandleMode.OVERWRITE);
        synchronized (threadList) {
            for (InstrumentedThread t : threadList) {
                try {
                    t.join();
                } catch (InterruptedException ie) {
                }
                if (t.callLog.size() > 0) {
                    logFile.writeln("Thread " + t.getName());
                    logFile.writeln(t.callLog.toString());
                }
            }
        }
        logFile.close();
    }
