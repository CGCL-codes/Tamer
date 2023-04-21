    private void writeData() {
        if (writeTask.ready()) {
            long writeTime = writeTask.getWriteTime();
            Iterator<Connection> iter = writeQueue.iterator();
            while (writeTime >= 0 && iter.hasNext()) {
                if (emptyQueueLastWrite) {
                    writeTime = 0;
                    emptyQueueLastWrite = false;
                }
                Connection connection = (Connection) iter.next();
                iter.remove();
                if (connection.isClosed()) {
                    continue;
                }
                connection.setInterestWrite();
                int bytesWritten = 0;
                try {
                    bytesWritten = connection.writeData();
                    bytesSent += bytesWritten;
                    logger.finest(connection + " Wrote " + bytesWritten + " bytes.");
                } catch (IOException e) {
                    connection.close("IO Exception occured while writing data.");
                }
                long writeWaitingTime = ((long) bytesWritten * (long) ONE_SECOND) / settings.getUploadSpeedMax();
                writeTime -= writeWaitingTime;
            }
            if (writeTime < 0) {
                writeTask = new WriteTask();
                writeTask.schedule(-writeTime);
            } else {
                writeTask.reset();
            }
            if (!iter.hasNext()) {
                emptyQueueLastWrite = true;
            }
        }
    }
