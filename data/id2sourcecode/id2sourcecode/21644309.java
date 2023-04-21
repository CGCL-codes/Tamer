    private void updatesFor0_7() {
        try {
            File downloadListFile = Environment.getInstance().getPhexConfigFile(EnvironmentConstants.XML_DOWNLOAD_FILE_NAME);
            if (downloadListFile.exists()) {
                FileUtils.copyFile(downloadListFile, new File(downloadListFile.getAbsolutePath() + ".v0.6.4"));
            }
        } catch (IOException exp) {
            Logger.logError(exp);
        }
        runningPhexVersion = "0.7";
    }
