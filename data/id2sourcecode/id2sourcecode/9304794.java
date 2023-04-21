    public void recover(Map heuristicallyCompletedTransactions) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(logFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            if (fis.available() < LogRecord.FULL_HEADER_LEN) return;
            FileChannel channel = fis.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(LogRecord.FULL_HEADER_LEN);
            channel.read(buf);
            int len = LogRecord.getNextRecordLength(buf, 0);
            LogRecord.HeurData data = new LogRecord.HeurData();
            while (len > 0) {
                buf = ByteBuffer.allocate(len + LogRecord.FULL_HEADER_LEN);
                if (channel.read(buf) < len) break;
                buf.flip();
                LogRecord.getHeurData(buf, len, data);
                switch(data.recordType) {
                    case LogRecord.HEUR_STATUS:
                        heuristicallyCompletedTransactions.put(new Long(data.localTransactionId), data);
                        break;
                    case LogRecord.HEUR_FORGOTTEN:
                        heuristicallyCompletedTransactions.remove(new Long(data.localTransactionId));
                        break;
                    default:
                        break;
                }
                len = LogRecord.getNextRecordLength(buf, len);
            }
        } catch (IOException ignore) {
        }
        try {
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
