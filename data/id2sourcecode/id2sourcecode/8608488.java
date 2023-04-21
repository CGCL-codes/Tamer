    public boolean rollbackSystemFiles(String appId) {
        logger.debug("Rolling back files for Application Id: " + appId);
        boolean rolledBack = true;
        String destDir = RTClientProperties.instance().getFilesDir() + File.separator + appId;
        File tmpDir = this.getTmpFileDir(appId);
        if (tmpDir.exists() == true && tmpDir.isDirectory() == true) {
            try {
                for (File file : tmpDir.listFiles()) {
                    String dest = destDir + File.separator + file.getName();
                    CopyFile.copyFile(file, dest);
                }
            } catch (IOException ex) {
                rolledBack = false;
                logger.warn(ex);
            }
        } else logger.debug("The temp directory for: " + appId + " was not found or not a directory");
        return rolledBack;
    }
