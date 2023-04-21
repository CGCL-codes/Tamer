    private void doAsciiTransfer() throws ReadFromSourceFileFailure, WriteToTargetFileFailure, TransferCancelledExc {
        logger.info("transferring a texrt file from " + this.sourceFile.getURL() + " to " + this.targetFile.getURL() + "...");
        this.statusString = "transferring....";
        this.notifyStatusUpdate();
        this.timeWhenTransferWasStarted = new java.util.Date().getTime();
        boolean transferComplete = false;
        while (!transferComplete) {
            if (this.cancelTransferRequested) {
                throw new TransferCancelledExc(this.sourceFile, this.targetFile);
            }
            String nextLine;
            try {
                nextLine = this.inputStream.readLine();
            } catch (java.io.IOException e) {
                throw new ReadFromSourceFileFailure(e, this.sourceFile);
            }
            if (nextLine == null) {
                transferComplete = true;
                break;
            }
            try {
                this.outputStream.writeLine(nextLine);
            } catch (java.io.IOException e) {
                throw new WriteToTargetFileFailure(e, this.targetFile);
            }
        }
    }
