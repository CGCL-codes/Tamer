    private boolean getFileAccess() {
        return (writeLocalFileAccess() && readRemoteFileAccess());
    }
