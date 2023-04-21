    private boolean setUploadStreams() {
        try {
            if (compression) {
                fileTransferOutputStream = new GZIPOutputStream(session.getClient().getConnection().getFileTransferDataOutputStream());
            } else {
                fileTransferOutputStream = session.getClient().getConnection().getFileTransferDataOutputStream();
            }
            if (resume) {
                fileTransferRandomAccessFile.seek(remoteFileSize);
            }
            fileTransferFileInputStream = Channels.newInputStream(fileTransferRandomAccessFile.getChannel());
            return true;
        } catch (Exception e) {
            try {
                fileTransferOutputStream.close();
            } catch (Exception e1) {
            }
            try {
                fileTransferFileInputStream.close();
            } catch (Exception e1) {
            }
            return false;
        }
    }
