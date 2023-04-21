    private boolean tryUpload() {
        SAWTerminal.print("\nSAW>SAWFILETRANSFER:Trying to send file...\nSAW>");
        try {
            fileTransferRandomAccessFile = new RandomAccessFile(fileTransferFile, "r");
            fileTransferRandomAccessFile.getChannel().lock();
        } catch (Exception e) {
        }
        if (verifyUpload()) {
            return (setUploadStreams() && uploadFileData());
        }
        return false;
    }
