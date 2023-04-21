    private boolean tryDownload() {
        SAWTerminal.print("\nSAW>SAWFILETRANSFER:Trying to receive file...\nSAW>");
        try {
            fileTransferRandomAccessFile = new RandomAccessFile(fileTransferFile, "rw");
            fileTransferRandomAccessFile.getChannel().lock();
        } catch (Exception e) {
        }
        if (verifyDownload()) {
            return (setDownloadStreams() && downloadFileData());
        } else {
            return false;
        }
    }
