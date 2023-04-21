    private boolean getFileStatus() {
        return (writeLocalFileStatus() && readRemoteFileStatus());
    }
