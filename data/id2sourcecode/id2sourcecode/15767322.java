    public void recover(List committedSingleTmTransactions, List committedMultiTmTransactions, List inDoubtTransactions, List inDoubtJcaTransactions) {
        Map activeMultiTmTransactions = new HashMap();
        FileInputStream fis;
        DataInputStream dis;
        CorruptedLogRecordException corruptedLogRecordException = null;
        try {
            fis = new FileInputStream(logFile);
            dis = new DataInputStream(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            readHeaderObject(dis);
            if (fis.available() < LogRecord.FULL_HEADER_LEN) return;
            FileChannel channel = fis.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(LogRecord.FULL_HEADER_LEN);
            channel.read(buf);
            LogRecord.Data data;
            int len = LogRecord.getNextRecordLength(buf, 0);
            while (len > 0) {
                buf = ByteBuffer.allocate(len + LogRecord.FULL_HEADER_LEN);
                if (channel.read(buf) < len) {
                    errorLog.info("Unexpected end of file in transaction log file " + logFile.getName());
                    break;
                }
                buf.flip();
                data = new LogRecord.Data();
                try {
                    LogRecord.getData(buf, len, data);
                } catch (CorruptedLogRecordException e) {
                    if (corruptedLogRecordException == null) corruptedLogRecordException = e;
                    long corruptedRecordPos = channel.position() - buf.limit() - LogRecord.FULL_HEADER_LEN;
                    long nextPos = scanForward(corruptedRecordPos + 1);
                    if (nextPos == 0) {
                        errorLog.info("LOG CORRUPTION AT THE END OF LOG FILE " + logFile.getName());
                        break;
                    } else {
                        errorLog.info("LOG CORRUPTION IN THE MIDDLE OF LOG FILE " + logFile.getName() + ". Skipping " + (nextPos - corruptedRecordPos) + " bytes" + ". Disabling presumed rollback.");
                        channel.position(nextPos);
                        buf = ByteBuffer.allocate(LogRecord.FULL_HEADER_LEN);
                        channel.read(buf);
                        len = LogRecord.getNextRecordLength(buf, 0);
                        corruptedLogRecordException.disablePresumedRollback = true;
                        continue;
                    }
                }
                switch(data.recordType) {
                    case LogRecord.TX_COMMITTED:
                        data.globalTransactionId = xidFactory.localIdToGlobalId(data.localTransactionId);
                        committedSingleTmTransactions.add(data);
                        break;
                    case LogRecord.MULTI_TM_TX_COMMITTED:
                        data.globalTransactionId = xidFactory.localIdToGlobalId(data.localTransactionId);
                    case LogRecord.TX_PREPARED:
                    case LogRecord.JCA_TX_PREPARED:
                        activeMultiTmTransactions.put(new Long(data.localTransactionId), data);
                        break;
                    case LogRecord.TX_END:
                        activeMultiTmTransactions.remove(new Long(data.localTransactionId));
                        break;
                    default:
                        errorLog.warn("INVALID TYPE IN LOG RECORD.");
                        break;
                }
                try {
                    len = LogRecord.getNextRecordLength(buf, len);
                } catch (CorruptedLogRecordException e) {
                    if (corruptedLogRecordException == null) corruptedLogRecordException = e;
                    long corruptedRecordPos = channel.position() - buf.limit() - LogRecord.FULL_HEADER_LEN;
                    long nextPos = scanForward(corruptedRecordPos + 1);
                    if (nextPos == 0) {
                        errorLog.info("LOG CORRUPTION AT THE END OF LOG FILE " + logFile.getName());
                        len = 0;
                    } else {
                        errorLog.info("LOG CORRUPTION IN THE MIDDLE OF LOG FILE " + logFile.getName() + ". Skipping " + (nextPos - corruptedRecordPos) + " bytes" + ". Disabling presumed rollback.");
                        channel.position(nextPos);
                        buf = ByteBuffer.allocate(LogRecord.FULL_HEADER_LEN);
                        channel.read(buf);
                        len = LogRecord.getNextRecordLength(buf, 0);
                        corruptedLogRecordException.disablePresumedRollback = true;
                    }
                }
            }
            Iterator iter = activeMultiTmTransactions.values().iterator();
            while (iter.hasNext()) {
                data = (LogRecord.Data) iter.next();
                switch(data.recordType) {
                    case LogRecord.MULTI_TM_TX_COMMITTED:
                        committedMultiTmTransactions.add(data);
                        break;
                    case LogRecord.TX_PREPARED:
                        inDoubtTransactions.add(data);
                        break;
                    case LogRecord.JCA_TX_PREPARED:
                        inDoubtJcaTransactions.add(data);
                        break;
                    default:
                        errorLog.warn("INCONSISTENT STATE.");
                        break;
                }
            }
            if (corruptedLogRecordException != null) throw corruptedLogRecordException;
        } catch (IOException e) {
            errorLog.warn("Unexpected exception in recover:", e);
        } catch (ClassNotFoundException e) {
            errorLog.warn("Unexpected exception in recover:", e);
        }
        try {
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
