    public static void saveLogs(String filename, ThreadGroup group) throws IOException {
        FileHandler logFile = new FileHandler(filename, FileHandler.HandleMode.OVERWRITE);
        ArrayList<InstrumentedThread> groupThreadList = groupThreadLists.get(group.getName());
        if (groupThreadList != null) {
            synchronized (groupThreadList) {
                for (InstrumentedThread t : groupThreadList) {
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
        }
        logFile.close();
    }
