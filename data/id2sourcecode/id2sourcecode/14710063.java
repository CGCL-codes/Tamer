    private DataOutputStream getLogFile() throws LoggerException {
        FileOutputStream fos = null;
        String uniqueId = "";
        int tries = 0;
        String logDirectory = null;
        String filePrefix = null;
        String fileSuffix = null;
        filePrefix = "JProbe";
        fileSuffix = "log";
        while (true) {
            String logFileName = filePrefix + "." + fileSuffix;
            try {
                fos = new FileOutputStream(logFileName, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new LoggerException("Error Creating File Stream", e.getMessage());
            }
            if (!lockLogFile) break;
            FileChannel channel = fos.getChannel();
            try {
                FileLock fl = channel.tryLock();
                if (fl != null) break;
            } catch (IOException e) {
                e.printStackTrace();
                throw new LoggerException("Error Locking Log File", e.getMessage());
            }
            uniqueId = "(" + ++tries + ")";
            if (tries > 50) throw new LoggerException("Max Retries Exceeded", "");
        }
        return new DataOutputStream(fos);
    }
