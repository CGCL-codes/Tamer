    private BufferedChannel getChannelForLogId(long entryLogId) throws IOException {
        BufferedChannel fc = channels.get(entryLogId);
        if (fc != null) {
            return fc;
        }
        File file = findFile(entryLogId);
        FileChannel newFc = new RandomAccessFile(file, "rw").getChannel();
        synchronized (channels) {
            fc = channels.get(entryLogId);
            if (fc != null) {
                newFc.close();
                return fc;
            }
            fc = new BufferedChannel(newFc, 8192);
            channels.put(entryLogId, fc);
            return fc;
        }
    }
