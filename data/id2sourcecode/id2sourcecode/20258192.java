    private int receiveDataFromServer() {
        File file = new File(writeToFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e1) {
        }
        if (fileLength > 10240) {
            long perLength = fileLength / (long) numberOfThreads;
            threads = new Thread[numberOfThreads + 1];
            long tempstart = 0, remainLen = fileLength;
            int i = 0;
            while (i < numberOfThreads && remainLen > perLength) {
                logger.info("[MClientMain] Create Thread:" + (i + 1));
                logger.info("[MClientMain] Begin: " + tempstart + "   Length: " + perLength);
                threads[i] = new Thread(new TransferThreadClient.Builder().setHostname(hostname).setPort(port).setRequiredFileName(requiredFileName).setStartPos(tempstart).setTransferLength(perLength).setWriteToFileName(writeToFileName).setTransferLengthInfo(transferLengthInfo).build());
                threads[i].start();
                tempstart += perLength;
                remainLen -= perLength;
                ++i;
            }
            if (remainLen > 0) {
                logger.info("[MClientMain] Create Thread:" + (i + 1));
                logger.info("[MClientMain] Begin: " + tempstart + "   Length: " + remainLen);
                threads[i] = new Thread(new TransferThreadClient.Builder().setHostname(hostname).setPort(port).setRequiredFileName(requiredFileName).setStartPos(tempstart).setTransferLength(remainLen).setWriteToFileName(writeToFileName).setTransferLengthInfo(transferLengthInfo).build());
                threads[i].start();
            }
        } else {
            new Thread(new TransferThreadClient.Builder().setHostname(hostname).setPort(port).setRequiredFileName(requiredFileName).setStartPos(0).setTransferLength(fileLength).setWriteToFileName(writeToFileName).setTransferLengthInfo(transferLengthInfo).build()).start();
        }
        Thread acks = new Thread(new Acks());
        acks.start();
        try {
            acks.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return TRANSFER_SUCCESS;
    }
