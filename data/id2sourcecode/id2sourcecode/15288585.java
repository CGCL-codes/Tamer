    protected void assembleEARDeployerProduct() throws IOException {
        String destEARDeployerFilename = getDescriptor().getAttribute(ATTRIBUTE_DEST_EARDEPLOYER).getValue();
        File destEARDeployerFile = new File(getPluginTmpDir(), destEARDeployerFilename);
        String sourceEARDeployer = getDescriptor().getAttribute(ATTRIBUTE_SOURCE_EARDEPLOYER).getValue();
        File sourceEARDeployerFile = getFilePath(sourceEARDeployer);
        if (!destEARDeployerFile.exists() || sourceEARDeployerFile.lastModified() > destEARDeployerFile.lastModified()) {
            logger.debug(destEARDeployerFile.getPath() + " was replaced since its source files are more recent");
            logger.debug("Copy " + sourceEARDeployerFile.getPath() + " to " + destEARDeployerFile);
            FileUtils.copyFile(sourceEARDeployerFile, destEARDeployerFile);
        } else {
            logger.debug(destEARDeployerFile.getPath() + " is more recent than any of its source file: " + sourceEARDeployerFile.getPath());
        }
    }
