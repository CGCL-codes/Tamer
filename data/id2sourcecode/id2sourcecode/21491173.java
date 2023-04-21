    HeuristicStatusLog(File dir) throws IOException {
        logFile = File.createTempFile("HEURISTIC_STATUS_LOG", ".log", dir);
        os = new RandomAccessFile(logFile, "rw");
        channel = os.getChannel();
        channel.force(true);
    }
