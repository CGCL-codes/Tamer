    public void downloadSelectedUpdates() {
        File tempDir = ShowDownManager.INSTANCE.getSDTempDirectory();
        tempDir = new File(tempDir, Long.toString(System.currentTimeMillis()));
        ShowDownLog.getInstance().logDebug("Downloading Update files as needed.");
        DownloadAndUpdateThread daut = new DownloadAndUpdateThread(tempDir, getFilesForDelete(), getOverwriteFiles(), getUpdateParsers(), getUpdateFeeds(), getUpdateShows());
        if (asynchronous) {
            daut.start();
        } else {
            daut.run();
        }
    }
