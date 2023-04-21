        public void run() {
            {
                if ((busyFlag0V == false) && (waitingRecordList0.size() > 0)) {
                    busyFlag0V = true;
                    boolean done = false;
                    while (waitingRecordList0.size() > 0) {
                        if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write("this is in writing thread: " + recordCount);
                        recordCount++;
                        wRecordBlock = (WaitingRecordBlock) waitingRecordList0.get(0);
                        currentLogFileWriter.createSAWSRecord(wRecordBlock.messageBlock, wRecordBlock.recordType, wRecordBlock.userID, wRecordBlock.encryptionFlag, null);
                        waitingRecordList0.remove(0);
                        done = true;
                    }
                    if (done) {
                        tcbContent.setTCBContent(latestLogFilename, currentLogFileWriter.getCurrentRecordWriteCount(), currentLogFileWriter.getAccHash());
                        tcbContent.write();
                        if (debugLevel > SAWSConstant.NoInfo) {
                            sawsDebugLog.write("TCB written.");
                        }
                        currentTime = System.currentTimeMillis();
                        if (recordCount > signRecordNumber) {
                            startANewLog();
                        }
                    }
                    busyFlag0V = false;
                }
                if ((busyFlag1V == false) && (waitingRecordList1.size() > 0)) {
                    busyFlag1V = true;
                    boolean done = false;
                    while (waitingRecordList1.size() > 0) {
                        if (debugLevel > SAWSConstant.NoInfo) sawsDebugLog.write("this is in writing thread: " + recordCount);
                        recordCount++;
                        wRecordBlock = (WaitingRecordBlock) waitingRecordList1.get(0);
                        currentLogFileWriter.createSAWSRecord(wRecordBlock.messageBlock, wRecordBlock.recordType, wRecordBlock.userID, wRecordBlock.encryptionFlag, null);
                        waitingRecordList1.remove(0);
                        done = true;
                    }
                    if (done) {
                        tcbContent.setTCBContent(latestLogFilename, currentLogFileWriter.getCurrentRecordWriteCount(), currentLogFileWriter.getAccHash());
                        tcbContent.write();
                        if (debugLevel > SAWSConstant.NoInfo) {
                            sawsDebugLog.write("TCB written.");
                        }
                        currentTime = System.currentTimeMillis();
                        if (recordCount > signRecordNumber) {
                            startANewLog();
                        }
                    }
                    busyFlag1V = false;
                }
            }
        }
