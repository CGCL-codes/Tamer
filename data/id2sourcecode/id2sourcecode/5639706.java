    private boolean getFileChecksums() {
        return (writeLocalFileChecksum() && readRemoteFileChecksum());
    }
