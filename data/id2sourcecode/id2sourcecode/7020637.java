    public RecordStatus sendLogRecord(byte[] messageBlock, byte encryptionFlag) {
        if (this.closed) {
            return new RecordStatus(-1, SAWSConstant.LogFileClosed);
        }
        if ((encryptionFlag != SAWSConstant.NoEncryptionFlag) && (encryptionFlag != SAWSConstant.SymmetricEncryptionFlag) && (encryptionFlag != SAWSConstant.CommandFlag)) {
            return new RecordStatus(-1, SAWSConstant.InvalidEncryptionFlag);
        }
        String mBlock = new String(messageBlock);
        if ((encryptionFlag == (byte) SAWSConstant.CommandFlag) && (mBlock.compareTo("closeLogFile") == 0)) {
            while ((busyFlag0V == true) || (busyFlag1V == true)) {
                if (debugLevel > SAWSConstant.NoInfo) System.err.println("wait.............");
            }
            currentLogFileWriter.finalizeLogFile();
            tcbContent.setTCBContent(latestLogFilename, currentLogFileWriter.getCurrentRecordWriteCount(), currentLogFileWriter.getAccHash());
            tcbContent.write();
            this.closed = true;
            System.exit(0);
        }
        byte userID = (byte) 0x00;
        String userdn = null;
        if (SAWSInterface.compareTo("webservice") == 0) {
            userdn = getSSLDN();
            Byte B1 = (Byte) UserDNIDMap.get(userdn);
            if (B1 == null) return new RecordStatus(-1, SAWSConstant.UnauthorizedUser);
            userID = (byte) (B1.byteValue());
        }
        if (busyFlag0V == false) {
            if (debugLevel > SAWSConstant.NoInfo) {
                sawsDebugLog.write("\nThis is in sending thread when busyFlag0V == false");
            }
            busyFlag0V = true;
            WaitingRecordBlock wRecordBlock = new WaitingRecordBlock(messageBlock, SAWSConstant.SAWSClientLogDataType, encryptionFlag, userID);
            waitingRecordList0.addElement(wRecordBlock);
            busyFlag0V = false;
        } else {
            if (debugLevel > SAWSConstant.VerboseInfo) {
                sawsDebugLog.write("\nThis is in sending thread when busyFlag1V == false");
            }
            busyFlag1V = true;
            WaitingRecordBlock wRecordBlock = new WaitingRecordBlock(messageBlock, SAWSConstant.SAWSClientLogDataType, encryptionFlag, userID);
            waitingRecordList1.addElement(wRecordBlock);
            busyFlag1V = false;
        }
        while (writingHeartBeating) {
        }
        thread.run();
        return new RecordStatus(0, currentLogFileWriter.getCurrentRecordWriteCount());
    }
