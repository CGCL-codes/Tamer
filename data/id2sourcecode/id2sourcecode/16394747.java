    private FileChannel openChannel(long logId) throws FileNotFoundException {
        FileChannel logFile = new RandomAccessFile(new File(journalDirectory, Long.toHexString(logId) + ".txn"), "rw").getChannel();
        return logFile;
    }
