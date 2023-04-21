    private void createLogId(long logId) throws IOException {
        List<File> list = Arrays.asList(dirs);
        Collections.shuffle(list);
        File firstDir = list.get(0);
        if (logChannel != null) {
            logChannel.flush(true);
        }
        logChannel = new BufferedChannel(new RandomAccessFile(new File(firstDir, Long.toHexString(logId) + ".log"), "rw").getChannel(), 64 * 1024);
        logChannel.write((ByteBuffer) LOGFILE_HEADER.clear());
        channels.put(logId, logChannel);
        for (File f : dirs) {
            setLastLogId(f, logId);
        }
    }
